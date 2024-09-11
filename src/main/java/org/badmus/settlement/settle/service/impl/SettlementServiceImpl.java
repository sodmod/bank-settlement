package org.badmus.settlement.settle.service.impl;

import lombok.RequiredArgsConstructor;
import org.badmus.settlement.cloudinary.CloudinaryService;
import org.badmus.settlement.cloudinary.dto.CloudinaryResponseDTO;
import org.badmus.settlement.dto.SettlementDTO;
import org.badmus.settlement.kafka.producer.service.KafkaProducerService;
import org.badmus.settlement.settle.dto.KafkaFileDTO;
import org.badmus.settlement.settle.dto.SettlementExcelDto;
import org.badmus.settlement.settle.enums.SettlementEnum;
import org.badmus.settlement.settle.model.Settlement;
import org.badmus.settlement.settle.repo.SettlementRepository;
import org.badmus.settlement.settle.service.SettlementService;
import org.badmus.settlement.transaction.model.Transaction;
import org.badmus.settlement.transaction.repo.TransactionRepository;
import org.badmus.settlement.transaction.service.TransactionService;
import org.badmus.settlement.utils.ByteArrayMultipartFile;
import org.badmus.settlement.utils.GeneralUtils;
import org.badmus.settlement.utils.MultipartUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SettlementServiceImpl implements SettlementService {

    private final MultipartUtils multipartUtils;
    private final CloudinaryService cloudinaryService;
    private final TransactionRepository transactionRepository;
    private final KafkaProducerService kafkaProducerService;
    private final SettlementRepository settlementRepository;



    @Override
    public void postSettlement(@ModelAttribute SettlementDTO settlementDTO) {
        KafkaFileDTO kafkaFileDTO = new KafkaFileDTO();
        Map<String, CloudinaryResponseDTO> cloudinary = cloudinaryService.genericUpload(settlementDTO, CloudinaryResponseDTO.class);
        kafkaFileDTO.setFileUrl(cloudinary.get("response").getSecureUrl());
        kafkaFileDTO.setFileType(getFileExtension(settlementDTO.getFile()));
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

        // todo: loop through the list and implement validation and save logic
        excelDTO.forEach(settlementExcelDto -> {
            if(!transactionRepository.existsByRrnAndTransactionId(settlementExcelDto.getRrn(),
                    settlementExcelDto.getTransactionId())) {
                return;
            }
            Settlement settlement = Settlement.builder()
                    .description(settlementExcelDto.getRespondCode())
                    .settlementEnum(SettlementEnum.SETTLED)
                    .title("")
                    .build();
            settlementRepository.save(settlement);
        });

    }

    public String getFileExtension(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename != null && originalFilename.contains(".")) {
            return originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        }
        return "";
    }

    private void confirmSettlement(SettlementExcelDto settlementExcelDto) {


    }
}
