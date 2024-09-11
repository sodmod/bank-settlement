package org.badmus.settlement.cloudinary;

import org.badmus.settlement.cloudinary.dto.CloudinaryResponseDTO;
import org.badmus.settlement.dto.SettlementDTO;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface CloudinaryService {

    <K> Map<String, K> genericUpload(SettlementDTO settlementDTO, Class<K> classType);

    ByteArrayResource download(String cloudUrl);

    void deleteFolder(String folderName);

    void deleteSingleAsset(String public_id);



}
