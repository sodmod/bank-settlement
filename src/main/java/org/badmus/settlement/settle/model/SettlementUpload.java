package org.badmus.settlement.settle.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.badmus.settlement.settle.enums.SettlementUploadStatusEnum;

@Entity
@Table
@Getter
@Setter
public class SettlementUpload {

    @Id
    private long id;
    private int excelRows;
    private String fileName;
    private String processorType;
    private SettlementUploadStatusEnum settlementUploadStatusEnum;
    private String errorMessage;

}
