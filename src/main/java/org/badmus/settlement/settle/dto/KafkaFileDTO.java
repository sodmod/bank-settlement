package org.badmus.settlement.settle.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class KafkaFileDTO implements Serializable {

    private String fileUrl;
    private String fileType;

    public String toString() {
        return "{fileUrl\": " + fileUrl +
                ", fileType: " + fileType +
                "}";
    }

}
