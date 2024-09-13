package org.badmus.settlement.cloudinary.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class CloudinaryResponseDTO implements Serializable {

    private String fileId;
    private String secureUrl;
    private String responseMessage;
}
