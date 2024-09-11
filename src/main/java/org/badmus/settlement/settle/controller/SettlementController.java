package org.badmus.settlement.settle.controller;

import lombok.RequiredArgsConstructor;
import org.badmus.settlement.dto.SettlementDTO;
import org.badmus.settlement.settle.service.SettlementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/settle")
@RequiredArgsConstructor
public class SettlementController {
    private final SettlementService settlementService;

    @PostMapping
    public ResponseEntity<?> postSettlement(@ModelAttribute SettlementDTO settlementDTO) {
        settlementService.postSettlement(settlementDTO);
        return null;
    }
}
