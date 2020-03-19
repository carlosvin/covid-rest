package com.carlosvin.covid.services.csv;

import java.time.ZonedDateTime;

import com.carlosvin.covid.models.Country;
import com.carlosvin.covid.models.DateCountryStats;
import com.carlosvin.covid.models.DateStats;
import com.carlosvin.covid.services.CountriesProvider;
import com.carlosvin.covid.services.DateUtils;
import com.opencsv.bean.CsvBindByName;

public class CovidDataEntryCsv implements DateCountryStats {
	
    @CsvBindByName(column = "Country/Region", required = true)
    private String country;
    
    private ZonedDateTime date;
    
    @CsvBindByName(column = "Confirmed")
    private int confirmed;
    
    @CsvBindByName(column = "Deaths")
    private int deaths;
    
    @CsvBindByName(column = "Recovered")
    private int recovered;

	@Override
	public DateStats getDateStats() {
		return new DateStats() {
			
			@Override
			public int getDeaths() {
				return deaths;
			}
			
			@Override
			public int getConfirmed() {
				return confirmed;
			}
			
			@Override
			public ZonedDateTime getDate() {
				return date;
			}
		};
	}
	
	@Override
	public Country getCountry() {
		return new Country() {
			
			@Override
			public String getCountry() {
				return country;
			}
			
			@Override
			public String getCode() {
				return CountriesProvider.get(country);
			}
		};
	}

	public CovidDataEntryCsv setDate(long epochDays) {
		this.date = DateUtils.convert(epochDays);
		return this;
	}

}
