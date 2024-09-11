package org.badmus.settlement.settle.service;

import org.badmus.settlement.dto.SettlementDTO;

public interface SettlementService {
    void postSettlement(SettlementDTO settlementDTO);
    void consumeSettlementFromKafkaString(String consumeMessage);
}
