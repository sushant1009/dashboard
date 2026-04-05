    package com.finance.dashboard.controller;

    import com.finance.dashboard.dto.DashboardSummaryDTO;
    import com.finance.dashboard.dto.TransactionDTO;
    import com.finance.dashboard.entity.TransactionType;
    import com.finance.dashboard.service.TransactionService;
    import lombok.RequiredArgsConstructor;
    import org.springframework.security.access.prepost.PreAuthorize;
    import org.springframework.security.core.Authentication;
    import org.springframework.web.bind.annotation.*;
    import java.time.LocalDate;
    import java.util.List;
    import java.util.Map;

    @RestController
    @RequestMapping("/dashboard")
    @RequiredArgsConstructor
    public class DashboardController {

        private final TransactionService transactionService;

        // FILTER (ANALYST + ADMIN ONLY)
        @GetMapping("/filter")
        @PreAuthorize("hasAnyRole('ADMIN','ANALYST')")
        public List<TransactionDTO> filter(
                @RequestParam(required = false) TransactionType type,
                @RequestParam(required = false) String category,
                @RequestParam(required = false) LocalDate start,
                @RequestParam(required = false) LocalDate end) {

            return transactionService.filterTransactions(type, category.toLowerCase(), start, end);
        }

        //  SUMMARY (ALL ROLES, BUT DIFFERENT DATA)
        @GetMapping("/summary")
        public DashboardSummaryDTO getSummary(
                @RequestParam(required = false) LocalDate start,
                @RequestParam(required = false) LocalDate end,
                Authentication auth) {
            if (start != null && end != null && start.isAfter(end)) {
                throw new IllegalArgumentException(
                        "Start date must be before or equal to end date"
                );
            }


            boolean isViewer = auth.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_VIEWER"));

            if (isViewer) {
                Long userId = Long.valueOf(auth.getName());
                return transactionService.getSummaryForUserWithFilter(userId,start,end);
            }

            // Analyst + Admin
            return transactionService.getSummaryWithFilter(start,end);
        }

        //User specific transactions summary(Admin + Analyst)
        @PreAuthorize("hasAnyRole('ADMIN','ANALYST')")
        @GetMapping("/summary/{userId}")
        public DashboardSummaryDTO getSummaryForUserWithFilter(@RequestParam(required = false) LocalDate start,
                                              @RequestParam(required = false) LocalDate end,
                                              @PathVariable Long userId) {
            if (start != null && end != null && start.isAfter(end)) {
                throw new IllegalArgumentException(
                        "Start date must be before or equal to end date"
                );
            }

            return transactionService.getSummaryForUserWithFilter(userId,start,end);
        }

        //  CATEGORY SUMMARY
        @GetMapping("/category")
        public Map<String, Double> categorySummary( @RequestParam(required = false) TransactionType type,
                                                    @RequestParam(required = false) String category,
                                                    @RequestParam(required = false) LocalDate start,
                                                    @RequestParam(required = false) LocalDate end,
                                                    Authentication auth) {
            if (start != null && end != null && start.isAfter(end)) {
                throw new IllegalArgumentException(
                        "Start date must be before or equal to end date"
                );
            }

            boolean isViewer = auth.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_VIEWER"));

            if (isViewer) {
                Long userId = Long.valueOf(auth.getName());

                return transactionService.getCategorySummaryForUserWithFilter(userId,type,category,start,end);
            }

            return transactionService.getCategorySummaryWithFilter(type,category,start,end);
        }

        // User Specific category summary(Admin + Analyst)
        @PreAuthorize("hasAnyRole('ADMIN','ANALYST')")
        @GetMapping("/category/{userId}")
        public Map<String, Double> categorySummaryForUserWithFilter(@RequestParam(required = false) TransactionType type,
                                                   @RequestParam(required = false) String category,
                                                   @RequestParam(required = false) LocalDate start,
                                                   @RequestParam(required = false) LocalDate end,
                                                   @PathVariable Long userId) {
            if (start != null && end != null && start.isAfter(end)) {
                throw new IllegalArgumentException(
                        "Start date must be before or equal to end date"
                );
            }

            return transactionService.getCategorySummaryForUserWithFilter(userId,type,category,start,end);

        }
    }