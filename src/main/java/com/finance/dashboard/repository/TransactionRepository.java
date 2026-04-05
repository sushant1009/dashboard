package com.finance.dashboard.repository;

import com.finance.dashboard.entity.Transaction;
import com.finance.dashboard.entity.TransactionType;
import com.finance.dashboard.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByType(TransactionType type);

    List<Transaction> findByCategory(String category);

    List<Transaction> findByDateBetween(LocalDate start, LocalDate end);

    List<Transaction> findByUser(User user);

}
