package com.carlosvin.covid.controllers;

import java.time.ZonedDateTime;

import javax.validation.constraints.NotBlank;

import com.carlosvin.covid.models.DateStats;

public class CovidDateEntryDto implements DateStats {
	
	private final ZonedDateTime date;
	private final String country;
	private final String countryCode;
	private final int deaths;
	private final int confirmed;

	public CovidDateEntryDto (ZonedDateTime date, String country, String countryCode, int newCases, int newDeaths) {
		this.date = date;
		this.country = country;
		this.countryCode = countryCode;
		this.confirmed = newCases;
		this.deaths = newDeaths;
	}


	@Override
	public int getConfirmed() {
		return this.confirmed;
	}

	@Override
	public int getDeaths() {
		return deaths;
	}

	@Override
	public ZonedDateTime getDate() {
		return date;
	}
}
