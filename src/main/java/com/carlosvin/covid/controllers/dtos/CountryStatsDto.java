package com.carlosvin.covid.controllers.dtos;

import com.carlosvin.covid.models.CountryStats;

public class CountryStatsDto extends StatsDto {
	
	public final String countryCode;
	public final String countryName;

	public CountryStatsDto (CountryStats stats) {
		super(stats);
		this.countryCode = stats.getCode();
		this.countryName = stats.getCountry();
	}
	
	public static class WithUrl extends CountryStatsDto {
		public final String path;

		public WithUrl (CountryStats stats, String basePath) {
			super(stats);
			path = basePath + "/" +  stats.getCode();
		}
	}
}
