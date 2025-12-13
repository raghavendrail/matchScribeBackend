package com.matchscribe.matchscribe_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.matchscribe.matchscribe_backend.config.SportsApiConfig;

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties(SportsApiConfig.class)
public class MatchScribeBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(MatchScribeBackendApplication.class, args);
	}

}
