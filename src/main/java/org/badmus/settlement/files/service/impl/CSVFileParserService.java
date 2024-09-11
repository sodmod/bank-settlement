package org.badmus.settlement.files.service.impl;

import lombok.RequiredArgsConstructor;
import org.badmus.settlement.cloudinary.CloudinaryService;
import org.badmus.settlement.cloudinary.dto.CloudinaryResponseDTO;
import org.badmus.settlement.dto.SettlementDTO;
import org.badmus.settlement.files.service.FileParser;
import org.badmus.settlement.files.type.FileTypes;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service(value = FileTypes.CSV)
@RequiredArgsConstructor
public class CSVFileParserService implements FileParser {

    private final CloudinaryService cloudinaryService;

    @Override
    public void parse(MultipartFile multipartFile) {
        Map<String, CloudinaryResponseDTO> objectMap = cloudinaryService.genericUpload(new SettlementDTO(), CloudinaryResponseDTO.class);

    }
}
