package com.carlosvin.covid.controllers;

import java.util.Date;

import javax.validation.constraints.NotBlank;

import com.carlosvin.covid.models.CovidDataEntry;

public class CovidDateEntryDto implements CovidDataEntry {
	
	private final long date;
	private final String country;
	private final String countryCode;
	private final int deaths;
	private final int confirmed;

	public CovidDateEntryDto (Date date, String country, String countryCode, int newCases, int newDeaths) {
		this.date = date.getTime();
		this.country = country;
		this.countryCode = countryCode;
		this.confirmed = newCases;
		this.deaths = newDeaths;
	}

	@Override
	public long getDate() {
		return this.date;
	}

	@Override
	public @NotBlank String getCountry() {
		return this.country;
	}

	@Override
	public int getConfirmed() {
		return this.confirmed;
	}

	@Override
	public int getNewDeaths() {
		return deaths;
	}

	@Override
	public @NotBlank String getCountryCode() {
		return countryCode;
	}

}
