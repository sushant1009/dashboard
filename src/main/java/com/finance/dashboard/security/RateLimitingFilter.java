package com.finance.dashboard.security;

import com.finance.dashboard.service.RateLimiterService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@AllArgsConstructor
public class RateLimitingFilter extends OncePerRequestFilter {


    private RateLimiterService rateLimiterService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String userKey;

        // ✅ Identify user (JWT or IP fallback)
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            userKey = SecurityContextHolder.getContext().getAuthentication().getName();
        } else {
            userKey = request.getRemoteAddr();
        }

        if (!rateLimiterService.isAllowed(userKey)) {
            response.setStatus(429);
            response.getWriter().write("Too many requests. Try again later.");
            return;
        }

        filterChain.doFilter(request, response);
    }


}
