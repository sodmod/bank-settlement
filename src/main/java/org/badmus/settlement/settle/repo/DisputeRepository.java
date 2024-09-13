package org.badmus.settlement.settle.repo;

import org.badmus.settlement.settle.model.Dispute;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DisputeRepository extends JpaRepository<Dispute, Long> {
}
