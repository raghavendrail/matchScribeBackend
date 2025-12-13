package com.matchscribe.matchscribe_backend.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "sportsapi")
public class SportsApiConfig {
    
    private String baseUrl;
    private String host;
    private String apiKey;
    private int timeoutMs;

    // Getters
    public String getBaseUrl() {
        return baseUrl;
    }

    public String getHost() {
        return host;
    }

    public String getApiKey() {
        return apiKey;
    }

    public int getTimeoutMs() {
        return timeoutMs;
    }

    // Setters
    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public void setTimeoutMs(int timeoutMs) {
        this.timeoutMs = timeoutMs;
    }

    @Override
    public String toString() {
        return "SportsApiConfig{" +
                "baseUrl='" + baseUrl + '\'' +
                ", host='" + host + '\'' +
                ", apiKey='[PROTECTED]'" +
                ", timeoutMs=" + timeoutMs +
                '}';
    }
}