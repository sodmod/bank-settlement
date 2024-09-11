package org.badmus.settlement.settle.repo;

import org.badmus.settlement.settle.model.Settlement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SettlementRepository extends JpaRepository<Settlement, Long> {
}
