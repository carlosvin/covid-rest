package com.carlosvin.covid.controllers.dtos;

import java.time.ZonedDateTime;

import com.carlosvin.covid.models.DateStats;
import com.carlosvin.covid.services.DateUtils;

public class DateStatsDto extends StatsDto {
	
	public final ZonedDateTime dateTime;
	public final String date;
	public final long epochSeconds;

	public DateStatsDto (DateStats stats) {
		super(stats);
		dateTime = stats.getDate();
		date = DateUtils.toIsoStr(dateTime);
		epochSeconds = dateTime.toEpochSecond();
	}
	
	public static class WithUrl extends DateStatsDto {
		public final String path;

		public WithUrl (DateStats stats, String basePath) {
			super(stats);
			path = basePath + "/" +  date;
		}
	}
}
