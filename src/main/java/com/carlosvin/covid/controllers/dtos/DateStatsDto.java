package com.carlosvin.covid.controllers.dtos;

import java.time.ZonedDateTime;

import com.carlosvin.covid.models.DateStats;

public class DateStatsDto extends StatsDto {
	
	public final ZonedDateTime date;
	public final long epochDays;

	public DateStatsDto (DateStats stats) {
		super(stats);
		date = stats.getDate();
		epochDays = date.toLocalDate().toEpochDay();
	}


}
