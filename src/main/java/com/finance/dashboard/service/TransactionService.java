package com.finance.dashboard.service;

import com.finance.dashboard.dto.CreateTransactionRequest;
import com.finance.dashboard.dto.DashboardSummaryDTO;
import com.finance.dashboard.dto.TransactionDTO;
import com.finance.dashboard.entity.Transaction;
import com.finance.dashboard.entity.TransactionType;
import com.finance.dashboard.entity.User;
import com.finance.dashboard.exception.ResourceNotFoundException;
import com.finance.dashboard.repository.TransactionRepository;
import com.finance.dashboard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    //Create transaction
    public TransactionDTO createTransaction(CreateTransactionRequest request) {

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Transaction transaction = Transaction.builder()
                .amount(request.getAmount())
                .type(request.getType())
                .category(request.getCategory().toLowerCase())
                .date(request.getDate())
                .note(request.getNote())
                .user(user)
                .build();

        return mapToDTO(transactionRepository.save(transaction));
    }

    //Update transaction
    public TransactionDTO updateTransaction(Long id, CreateTransactionRequest request) {

        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        transaction.setAmount(request.getAmount());
        transaction.setType(request.getType());
        transaction.setCategory(request.getCategory().toLowerCase());
        transaction.setDate(request.getDate());
        transaction.setNote(request.getNote());

        return mapToDTO(transactionRepository.save(transaction));
    }

    //Fetch all transactions
    public List<TransactionDTO> getAllTransactions() {
        return transactionRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    //Delete a transaction by Id
    public void deleteTransaction(Long id) {
        transactionRepository.deleteById(id);
    }

    private TransactionDTO mapToDTO(Transaction t) {
        return TransactionDTO.builder()
                .id(t.getId())
                .amount(t.getAmount())
                .type(t.getType())
                .category(t.getCategory())
                .date(t.getDate())
                .note(t.getNote())
                .userId(t.getUser().getId())
                .build();
    }



    //Filter All transactins by parameters
    public List<TransactionDTO> filterTransactions(
            TransactionType type,
            String category,
            LocalDate start,
            LocalDate end) {

        List<Transaction> transactions = transactionRepository.findAll();

        return transactions.stream()
                .filter(t -> type == null || t.getType() == type)
                .filter(t -> category == null ||
                        (t.getCategory() != null && t.getCategory().equalsIgnoreCase(category)))
                .filter(t -> start == null || !t.getDate().isBefore(start))
                .filter(t -> end == null || !t.getDate().isAfter(end))
                .map(this::mapToDTO)
                .toList();
    }

    //Filter user transactins by parameters
    public List<TransactionDTO> filterTransactionsForUser(
            Long userId,
            TransactionType type,
            String category,
            LocalDate start,
            LocalDate end) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        List<Transaction> transactions = transactionRepository.findByUser(user);


        return transactions.stream()
                .filter(t -> type == null || t.getType() == type)
                .filter(t -> category == null ||
                        (t.getCategory() != null && t.getCategory().equalsIgnoreCase(category)))
                .filter(t -> start == null || !t.getDate().isBefore(start))
                .filter(t -> end == null || !t.getDate().isAfter(end))
                .map(this::mapToDTO)
                .toList();
    }

    //Fetch all transactions made by a user
    public List<TransactionDTO> getTransactionsForUser(Long userId) {

        User user = userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User not found"));

        List<Transaction> transactions = transactionRepository.findByUser(user);

        return transactions.stream()
                .map(this::mapToDTO)
                .toList();
    }

   // ----------------------------Analytical Functions---------------------------------------------------

    //Transaction Summary for all users specified time period
    public DashboardSummaryDTO getSummaryWithFilter(
            LocalDate start,
            LocalDate end) {

        List<TransactionDTO> transactions =
                filterTransactions(null, null, start, end);

        double totalIncome = 0;
        double totalExpense = 0;

        for (TransactionDTO t : transactions) {
            if (t.getType() == TransactionType.INCOME) {
                totalIncome += t.getAmount();
            } else if (t.getType() == TransactionType.EXPENSE) {
                totalExpense += t.getAmount();
            }
        }

        double balance = totalIncome - totalExpense;

        return DashboardSummaryDTO.builder()
                .totalIncome(totalIncome)
                .totalExpense(totalExpense)
                .balance(balance)
                .build();
    }

    public DashboardSummaryDTO getSummaryForUserWithFilter(
            Long userId,
            LocalDate start,
            LocalDate end) {

        List<TransactionDTO> transactions =
                filterTransactionsForUser(userId, null, null, start, end);

        double totalIncome = 0;
        double totalExpense = 0;

        for (TransactionDTO t : transactions) {
            if (t.getType() == TransactionType.INCOME) {
                totalIncome += t.getAmount();
            } else {
                totalExpense += t.getAmount();
            }
        }

        double balance = totalIncome - totalExpense;

        return DashboardSummaryDTO.builder()
                .totalIncome(totalIncome)
                .totalExpense(totalExpense)
                .balance(balance)
                .build();
    }

    //Get category-wise Income or expenditure for all transactions
    public Map<String, Double> getCategorySummaryWithFilter(
            TransactionType type,
            String category,
            LocalDate start,
            LocalDate end) {

        List<TransactionDTO> transactions =
                filterTransactions(type, category, start, end);
        transactions.forEach((t)-> System.out.println(t.toString()));

        // Apply default ONLY when absolutely no filters are present
        if (type == null && category == null && start == null && end == null) {
            transactions = transactions.stream()
                    .filter(t -> t.getType() == TransactionType.EXPENSE)
                    .toList();
        }

        return transactions.stream()
                .filter(t -> t.getCategory() != null)
                .collect(Collectors.groupingBy(
                        TransactionDTO::getCategory,
                        Collectors.summingDouble(TransactionDTO::getAmount)
                ));
    }

    //Get category-wise Income or expenditure for transactions associated with a user
    public Map<String, Double> getCategorySummaryForUserWithFilter(
            Long userId,
            TransactionType type,
            String category,
            LocalDate start,
            LocalDate end) {


        List<TransactionDTO> transactions =
                filterTransactionsForUser(userId, type, category, start, end);


        if (type == null && category == null && start == null && end == null) {
            transactions = transactions.stream()
                    .filter(t -> t.getType() == TransactionType.EXPENSE)
                    .toList();
        }

        return transactions.stream()
                .filter(t -> t.getCategory() != null)
                .collect(Collectors.groupingBy(
                        TransactionDTO::getCategory,
                        Collectors.summingDouble(TransactionDTO::getAmount)
                ));
    }
}