package org.badmus.settlement.transaction.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Table
@Entity
@Getter
@Setter
public class Transaction {

    @Id
    private long id;
    private String rrn;
    private String responseCode;
    private String transactionId;
    private String responseMessage;

}
