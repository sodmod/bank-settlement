package org.badmus.settlement.transaction.service.impl;

import lombok.RequiredArgsConstructor;
import org.badmus.settlement.transaction.repo.TransactionRepository;
import org.badmus.settlement.transaction.service.TransactionService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;

}
