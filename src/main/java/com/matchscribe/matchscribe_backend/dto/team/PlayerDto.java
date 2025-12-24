package com.matchscribe.matchscribe_backend.dto.team;

import java.util.List;

import com.matchscribe.matchscribe_backend.entity.PlayerCareerBattingStats;
import com.matchscribe.matchscribe_backend.entity.PlayerCareerBowlingStats;

public class PlayerDto {
	public Long sl;
	public Long playerId;
	public String name;
	public String role;
	public String battingStyle;
	public String bowlingStyle;

	public List<PlayerCareerBattingStats> careerBattingStats;
	public List<PlayerCareerBowlingStats> careerBowlingStats;
}
