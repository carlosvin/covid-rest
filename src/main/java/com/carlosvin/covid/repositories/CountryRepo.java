package com.carlosvin.covid.repositories;

import java.time.LocalDate;
import java.util.TreeMap;

import com.carlosvin.covid.models.CountryStats;
import com.carlosvin.covid.models.DateCountryStats;
import com.carlosvin.covid.models.DateStats;
import com.carlosvin.covid.models.Stats;

public class CountryRepo implements Stats {
	
	TreeMap<String, CountryModel> countries = new TreeMap<String, CountryModel>();
	private int confirmed = 0, deaths = 0;
	
	public void add(DateCountryStats c) {
		CountryModel country = countries.get(c.getCountry().getId());
		if (country == null) {
			country = new CountryModel(c.getCountry());
			countries.put(c.getCountry().getId(), country);
		}
		country.add(c);
		confirmed += c.getDateStats().getConfirmed();
		deaths += c.getDateStats().getDeaths();
	}

	@Override
	public int getConfirmed() {
		return confirmed;
	}

	@Override
	public int getDeaths() {
		return deaths;
	}


	public CountryStats get(String countryCode) {
		return countries.get(countryCode);
	}

	public DateStats get(String countryCode, LocalDate date) {
		CountryModel c = countries.get(countryCode);
		if ( c!= null) {
			return c.get(date);
		}
		return null;
	}

	public Iterable<DateStats> getDates(String countryCode) {
		CountryModel c = countries.get(countryCode);
		if ( c!= null) {
			return c.getDates();
		}
		return null;
	}

	public Iterable<CountryModel> get() {
		return countries.values();
	}

}
