package org.badmus.settlement.settle.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.badmus.settlement.settle.enums.SettlementEnum;


@Table
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Settlement {

    @Id
    private long id;
    private String title;
    private String description;
    private SettlementEnum settlementEnum;

    public String toString() {
        return "{" +
                "id\": " + id +
                ",title: " + title+
                ",description: " + description +
                ",settlementEnum: " + settlementEnum +
                "}";
    }
}
