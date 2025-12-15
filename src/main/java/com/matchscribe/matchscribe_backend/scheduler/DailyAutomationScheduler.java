package com.matchscribe.matchscribe_backend.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.matchscribe.matchscribe_backend.service.DailyAutomationService;

@Component
public class DailyAutomationScheduler {
	private final DailyAutomationService automationService;

	public DailyAutomationScheduler(DailyAutomationService automationService) {
		this.automationService = automationService;
	}

	// Runs every day at 02:00 AM IST
	@Scheduled(cron = "0 0 2 * * ?", zone = "Asia/Kolkata")
	public void runDailyAutomation() {
		automationService.runDailyAutomation();
	}

}
