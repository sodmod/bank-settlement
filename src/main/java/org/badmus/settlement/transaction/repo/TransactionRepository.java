package org.badmus.settlement.transaction.repo;

import org.badmus.settlement.transaction.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Optional<Transaction> findTransactionsByRrnAndTransactionId(String rrn, String transactionId);

    boolean existsByRrnAndTransactionId(String rrn, String transactionId);
}
