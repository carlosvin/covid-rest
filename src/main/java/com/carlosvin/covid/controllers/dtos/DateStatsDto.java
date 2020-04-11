package com.carlosvin.covid.controllers.dtos;


import java.time.LocalDate;

import com.carlosvin.covid.models.DateStats;

public class DateStatsDto extends StatsDto {
	
	public final LocalDate date;
	public final long epochDay;

	public DateStatsDto (DateStats stats) {
		super(stats);
		date = stats.getDate();
		epochDay = date.toEpochDay();
	}
	
	public static class WithUrl extends DateStatsDto {
		public final String path;

		public WithUrl (DateStats stats, String basePath) {
			super(stats);
			path = basePath + "/" +  date;
		}
	}
}
