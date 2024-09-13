package org.badmus.settlement.settle.model;

import jakarta.persistence.*;
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
    @Enumerated(EnumType.STRING)
    private SettlementEnum settlementEnum;
    @OneToOne(cascade = CascadeType.ALL)
    private Dispute dispute;

    public String toString() {
        return "{" +
                "id\": " + id +
                ",title: " + title+
                ",description: " + description +
                ",settlementEnum: " + settlementEnum +
                "}";
    }
}
