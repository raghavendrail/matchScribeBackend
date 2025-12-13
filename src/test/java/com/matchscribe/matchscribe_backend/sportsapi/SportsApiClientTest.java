package com.matchscribe.matchscribe_backend.sportsapi;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.matchscribe.matchscribe_backend.config.SportsApiConfig;
import com.matchscribe.matchscribe_backend.integration.sportsapi.SportsApiClient;

@SpringBootTest
@ActiveProfiles("dev")
public class SportsApiClientTest {

	

    @Autowired
    private SportsApiClient sportsApiClient;
    
    @Autowired
    private SportsApiConfig sportsApiConfig;

    @Test
    public void testConfigurationLoaded() {
        // First verify configuration is loaded
    	System.out.println("\n******** Testing Configuration Loading ********");
    	System.out.println("SportsApiConfig: " + sportsApiConfig);
        Assertions.assertNotNull(sportsApiConfig);
        Assertions.assertNotNull(sportsApiConfig.getBaseUrl());
        Assertions.assertNotNull(sportsApiConfig.getApiKey());
        Assertions.assertNotNull(sportsApiConfig.getHost());
        
        System.out.println("\n===== Configuration =====");
        System.out.println("Base URL: " + sportsApiConfig.getBaseUrl());
        System.out.println("Host: " + sportsApiConfig.getHost());
        System.out.println("API Key: " + sportsApiConfig.getApiKey().substring(0, 10) + "...");
        System.out.println("Timeout: " + sportsApiConfig.getTimeoutMs() + "ms");
        System.out.println("========================\n");
    }

    @Test
    public void testGetUpcomingMatchesRaw() {
        String response = sportsApiClient.getUpcomingMatchesRaw();
        
        Assertions.assertNotNull(response, "Response should not be null");
        Assertions.assertFalse(response.isEmpty(), "Response should not be empty");
        
        System.out.println("\n===== RapidAPI Response Sample =====");
        System.out.println(response.substring(0, Math.min(500, response.length())));
        System.out.println("\n===================================\n");
    }
}