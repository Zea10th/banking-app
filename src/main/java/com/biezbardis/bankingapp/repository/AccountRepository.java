package com.biezbardis.bankingapp.repository;

import com.biezbardis.bankingapp.entity.Account;
import com.biezbardis.bankingapp.entity.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByClientIdAndCurrency(Long clientId, Currency currency);
    List<Account> findByClientId(Long clientId);
}
