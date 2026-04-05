package com.finance.dashboard.service;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RateLimiterService {

    private final Map<String, RequestInfo> requestCounts = new ConcurrentHashMap<>();

    private static final int MAX_REQUESTS = 10;
    private static final long TIME_WINDOW = 60 * 1000; // 1 minute

    public boolean isAllowed(String key) {

        long currentTime = System.currentTimeMillis();

        requestCounts.putIfAbsent(key, new RequestInfo(0, currentTime));

        RequestInfo info = requestCounts.get(key);

        // Reset window
        if (currentTime - info.startTime > TIME_WINDOW) {
            info.count = 0;
            info.startTime = currentTime;
        }

        info.count++;

        return info.count <= MAX_REQUESTS;
    }

    static class RequestInfo {
        int count;
        long startTime;

        public RequestInfo(int count, long startTime) {
            this.count = count;
            this.startTime = startTime;
        }
    }
}