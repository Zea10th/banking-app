package com.biezbardis.bankingapp.repository;

import com.biezbardis.bankingapp.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByAccountIdOrderByCreatedAtDesc(Long accountId);
}
