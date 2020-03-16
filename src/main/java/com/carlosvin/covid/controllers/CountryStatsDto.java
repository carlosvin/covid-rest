package com.carlosvin.covid.controllers;

import com.carlosvin.covid.models.CountryStats;

public class CountryStatsDto {
	
	public final String countryCode;
	public final String countryName;
	public final int confirmedCases;
	public final int deathsNumber;

	public CountryStatsDto (CountryStats stats) {
		this.countryCode = stats.getCode();
		this.countryName = stats.getCountry();
		this.confirmedCases = stats.getConfirmed();
		this.deathsNumber = stats.getDeaths();
	}
}
