package org.badmus.settlement.settle.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SettlementExcelDto {

    private String rrn;
    private String terminalId;
    private String respondCode;
    private String transactionId;
    private String processorType;


    public String toString() {
        return "{terminalId\": " + terminalId +
                ", rrn: " + rrn +
                ", processor: " + processorType +
                ", respondCode: " + respondCode +
                ", transactionId: " + transactionId +
                "}";
    }
}
