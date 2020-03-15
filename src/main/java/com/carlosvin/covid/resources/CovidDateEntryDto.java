package com.carlosvin.covid.resources;

import java.util.Date;

import javax.validation.constraints.NotBlank;

import com.carlosvin.covid.models.CovidDataEntry;

public class CovidDateEntryDto implements CovidDataEntry {
	
	private final long date;
	private final String country;
	private final int deaths;
	private final int confirmed;
	private final int recovered;

	public CovidDateEntryDto (Date date, String country, int recovered, int newCases, int newDeaths) {
		this.date = date.getTime();
		this.country = country;
		this.recovered = recovered;
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
	public int getRecovered() {
		return recovered;
	}

}
