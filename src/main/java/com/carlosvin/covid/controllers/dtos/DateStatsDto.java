package com.carlosvin.covid.controllers.dtos;

import java.time.ZonedDateTime;

import com.carlosvin.covid.models.DateStats;
import com.carlosvin.covid.services.DateUtils;

public class DateStatsDto extends StatsDto {
	
	public final ZonedDateTime date;
	public final long epochDays;
	public final String isoDateStr;

	public DateStatsDto (DateStats stats) {
		super(stats);
		date = stats.getDate();
		epochDays = date.toLocalDate().toEpochDay();
		isoDateStr = DateUtils.toIsoStr(date);
	}
	
	public static class WithUrl extends DateStatsDto {
		public final String path;

		public WithUrl (DateStats stats, String basePath) {
			super(stats);
			path = basePath + "/" +  isoDateStr;
		}
	}
}
