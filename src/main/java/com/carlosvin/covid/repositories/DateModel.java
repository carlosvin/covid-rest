package com.carlosvin.covid.repositories;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

import com.carlosvin.covid.models.Country;
import com.carlosvin.covid.models.CountryStats;
import com.carlosvin.covid.models.DateCountryStats;
import com.carlosvin.covid.models.DateStats;
import com.carlosvin.covid.models.Stats;

class DateModel implements DateStats {

	private final ZonedDateTime date;
	private final Map<String, CountryStats> countriesRepo;
	private int confirmed;
	private int deaths;

	public DateModel(ZonedDateTime zonedDateTime) {
		this.date = zonedDateTime;
		this.countriesRepo = new HashMap<>();
		this.confirmed = this.deaths = 0;
	}

	public DateModel(DateStats dateStats, Stats saved) {
		this(dateStats.getDate());
		this.confirmed = dateStats.getConfirmed() + saved.getConfirmed();
		this.deaths = dateStats.getDeaths() + saved.getDeaths();
	}

	public void add(DateCountryStats c) {
		Country country = c.getCountry();
		String id = country.getId();
		Stats saved = countriesRepo.get(id);
		if (saved == null) {
			countriesRepo.put(id, new CountryModel(country, c.getDateStats()));
		} else {
			countriesRepo.put(id, new CountryModel(country, saved, c.getDateStats()));
		}
	}
	
	public  ZonedDateTime getCountry() {
		return date;
	}

	@Override
	public int getConfirmed() {
		return confirmed;
	}

	@Override
	public int getDeaths() {
		return deaths;
	}
	@Override
	public ZonedDateTime getDate() {
		return date;
	}

	public Iterable<CountryStats> getCountries() {
		return countriesRepo.values();
	}
}
