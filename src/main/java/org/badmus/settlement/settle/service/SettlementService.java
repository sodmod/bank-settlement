package org.badmus.settlement.settle.service;

import org.badmus.settlement.dto.SettlementDTO;
import org.badmus.settlement.settle.model.Settlement;

public interface SettlementService {
    void postSettlement(SettlementDTO settlementDTO);

    void consumeSettlementFromKafkaString(String consumeMessage);

    Settlement saveSettlement(Settlement dispute);
}
