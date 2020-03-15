package com.carlosvin.covid.services;

import javax.validation.constraints.NotBlank;

import com.carlosvin.covid.models.CovidDataEntry;
import com.opencsv.bean.CsvBindByName;

public class CovidDataEntryCsv implements CovidDataEntry {
	
    @CsvBindByName(column = "Country/Region", required = true)
    private String country;
    
    private long date;
    
    @CsvBindByName(column = "Confirmed")
    private int confirmed;
    
    @CsvBindByName(column = "Deaths")
    private int deaths;
    
    @CsvBindByName(column = "Recovered")
    private int recovered;
    		
	@Override
	public long getDate() {
		return date;
	}

	@Override
	public @NotBlank String getCountry() {
		return country;
	}

	@Override
	public int getNewDeaths() {
		return deaths;
	}

	@Override
	public int getConfirmed() {
		return confirmed;
	}

	@Override
	public int getRecovered() {
		return recovered;
	}
	
	public CovidDataEntryCsv setDate(long date) {
		this.date = date;
		return this;
	}

}
