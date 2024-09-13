package org.badmus.settlement.settle.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.badmus.settlement.cloudinary.CloudinaryService;
import org.badmus.settlement.cloudinary.dto.CloudinaryResponseDTO;
import org.badmus.settlement.cloudinary.dto.ResponseMessage;
import org.badmus.settlement.dto.SettlementDTO;
import org.badmus.settlement.kafka.producer.service.KafkaProducerService;
import org.badmus.settlement.settle.dto.KafkaFileDTO;
import org.badmus.settlement.settle.dto.SettlementExcelDto;
import org.badmus.settlement.settle.enums.SettlementUploadStatusEnum;
import org.badmus.settlement.settle.model.Dispute;
import org.badmus.settlement.settle.model.Settlement;
import org.badmus.settlement.settle.model.SettlementUpload;
import org.badmus.settlement.settle.repo.DisputeRepository;
import org.badmus.settlement.settle.repo.SettlementRepository;
import org.badmus.settlement.settle.repo.SettlementUploadRepository;
import org.badmus.settlement.settle.service.DisputeService;
import org.badmus.settlement.settle.service.SettlementService;
import org.badmus.settlement.settle.service.SettlementUploadService;
import org.badmus.settlement.transaction.repo.TransactionRepository;
import org.badmus.settlement.utils.GeneralUtils;
import org.badmus.settlement.utils.MultipartUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.badmus.settlement.settle.enums.SettlementEnum.DISPUTE;
import static org.badmus.settlement.settle.enums.SettlementEnum.SETTLED;

@Slf4j
@Service
@RequiredArgsConstructor
public class SettlementServiceImpl implements SettlementService, SettlementUploadService, DisputeService {

    private final MultipartUtils multipartUtils;
    private final CloudinaryService cloudinaryService;
    private final DisputeRepository disputeRepository;
    private final KafkaProducerService kafkaProducerService;
    private final SettlementRepository settlementRepository;
    private final TransactionRepository transactionRepository;
    private final SettlementUploadRepository settlementUploadRepository;

    @Override
    public void postSettlement(@ModelAttribute SettlementDTO settlementDTO) {

        int row = processSettlementFiles(settlementDTO.getFile());

        Map<String, CloudinaryResponseDTO> cloudinary = cloudinaryService.genericUpload(settlementDTO, CloudinaryResponseDTO.class);

        SettlementUpload settlementUpload = new SettlementUpload();
        settlementUpload.setExcelRows(row);
        settlementUpload.setFileName(settlementDTO.getFile().getName());
        settlementUpload.setProcessorType(settlementDTO.getProcessorType());

        String cloudinaryResponseMessage = cloudinary.get("response").getResponseMessage();

        if (!cloudinaryResponseMessage.equalsIgnoreCase(ResponseMessage.SUCCESS.toString())) {
            settlementUpload.setSettlementUploadStatusEnum(SettlementUploadStatusEnum.FAILED);
            settlementUpload.setErrorMessage(cloudinaryResponseMessage);
            saveSettlementUpload(settlementUpload);
            throw new IllegalArgumentException("Cloudinary Exception Failed");
        }

        settlementUpload.setSettlementUploadStatusEnum(SettlementUploadStatusEnum.SUCCESS);
        saveSettlementUpload(settlementUpload);

        // kafka
        KafkaFileDTO kafkaFileDTO = new KafkaFileDTO();
        kafkaFileDTO.setFileUrl(cloudinary.get("response").getSecureUrl());
        kafkaFileDTO.setFileType(multipartUtils.getFileExtension(settlementDTO.getFile()));
        kafkaProducerService.sendMessageToTopic("", kafkaFileDTO.toString());
    }

    @Override
    public void consumeSettlementFromKafkaString(String consumeMessage) {
        // convert the string to dto
        KafkaFileDTO kafkaFileDTO = GeneralUtils.convertStringToObject(consumeMessage, KafkaFileDTO.class);

        // download from cloudinary and get the byteArrayResponse
        ByteArrayResource byteArrayResource = cloudinaryService.download(kafkaFileDTO.getFileUrl());

        // convert the bytearray resource into excel dto
        List<SettlementExcelDto> excelDTO = multipartUtils.convertExcelToDTO(byteArrayResource,
                SettlementExcelDto.class, kafkaFileDTO.getFileType());

        // loop through the list and implement validation and save entity
        excelDTO.forEach(settlementExcelDto -> {
            Settlement settlement = new Settlement();
            settlement.setDescription(settlementExcelDto.getRespondCode());
            settlement.setTitle("");
            if (!transactionRepository.existsByRrnAndTransactionId(settlementExcelDto.getRrn(),
                    settlementExcelDto.getTransactionId())) {
                settlement.setDispute(new Dispute());
                settlement.setSettlementEnum(DISPUTE);
            } else
                settlement.setSettlementEnum(SETTLED);
            saveSettlement(settlement);
        });
    }

    @Override
    public Settlement saveSettlement(Settlement settlement) {
        return settlementRepository.save(settlement);
    }

    @Override
    public SettlementUpload saveSettlementUpload(SettlementUpload settlementUpload) {
        return settlementUploadRepository.save(settlementUpload);
    }

    @Override
    public Dispute saveDispute(Dispute dispute) {
        return disputeRepository.save(dispute);
    }

    /**
     * This method below accepts multipartFile of csv, xls and xlsx types and does the following:=>
     * 1. check if file is valid either csv or xls or xlsx,
     * 2. confirm the sheet columns name,
     * 3. know the total numbers of rows in the sheet
     * 4. And stores the SettlementUpload entity
     */
    private int processSettlementFiles(MultipartFile multipartFile) {
        String filename = Objects.requireNonNull(multipartFile.getOriginalFilename());
        return switch (filename) {
            case "xlsx", "xls", "csv" -> {
                multipartUtils.checkExcelSheetHeaders(multipartFile);
                yield multipartUtils.countExcelRows(multipartFile);
            }
            default -> throw new IllegalArgumentException("file type is not supported");
        };

    }

}
