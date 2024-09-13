package org.badmus.settlement.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

@Data
public class SettlementDTO implements Serializable {
    private String publicId;
    private MultipartFile file;
    private String processorType;
}
