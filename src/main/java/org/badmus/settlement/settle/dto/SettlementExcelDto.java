package org.badmus.settlement.settle.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SettlementExcelDto {

    private String terminalId;
    private String rrn;
    private String processor;
    private String respondCode;
    private String transactionId;

    public String toString() {
        return "{terminalId\": " + terminalId +
                ", rrn: " + rrn +
                ", processor: " + processor +
                ", respondCode: " + respondCode +
                ", transactionId: " + transactionId +
                "}";
    }
}
