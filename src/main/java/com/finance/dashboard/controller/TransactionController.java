package com.finance.dashboard.controller;

import com.finance.dashboard.dto.CreateTransactionRequest;
import com.finance.dashboard.dto.TransactionDTO;
import com.finance.dashboard.service.TransactionService;
import com.finance.dashboard.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;
    private final UserService userService;

    //Create Transactions
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/create")
    public TransactionDTO create(@Valid @RequestBody CreateTransactionRequest request) {
        return transactionService.createTransaction(request);
    }

    //Get all transactions
    @GetMapping("/")
    @PreAuthorize("hasAnyRole('ADMIN','ANALYST','VIEWER')")
    public List<TransactionDTO> getAllTransactions(Authentication authentication) {

        boolean isViewer = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_VIEWER"));

        if (isViewer) {
            Long userId = Long.valueOf(authentication.getName());
            return transactionService.getTransactionsForUser(userId);
        }

        return transactionService.getAllTransactions();
    }

    //Delete transactions
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/admin/{id}")
    public void delete(@PathVariable Long id) {
        transactionService.deleteTransaction(id);
    }

    //Update transactions
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin/{id}")
    public TransactionDTO update(@PathVariable Long id,
                                 @RequestBody CreateTransactionRequest request) {
        return transactionService.updateTransaction(id, request);

    }

}