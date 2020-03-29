package com.carlosvin.covid.repositories;

import java.time.ZonedDateTime;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Stream;

import com.carlosvin.covid.models.Country;
import com.carlosvin.covid.models.CountryStats;
import com.carlosvin.covid.models.DateCountryStats;
import com.carlosvin.covid.models.DateStats;
import com.carlosvin.covid.models.Stats;

class DateModel implements DateStats {

	private final ZonedDateTime date;
	private final SortedMap<String, CountryStats> countriesRepo;
	private int confirmed;
	private int deaths;

	public DateModel(ZonedDateTime zonedDateTime) {
		this.date = zonedDateTime;
		this.countriesRepo = new TreeMap<>();
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
		Stats stats = c.getDateStats();
		Stats saved = countriesRepo.get(id);
		if (saved == null) {
			countriesRepo.put(id, new CountryModel(country, stats));
		} else {
			countriesRepo.put(id, new CountryModel(country, saved, stats));
		}
		confirmed += stats.getConfirmed();
		deaths += stats.getDeaths();
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

	public Stream<CountryStats> getCountries() {
		return countriesRepo.values().stream();
	}

	public CountryStats get(String countryCode) {
		return countriesRepo.get(countryCode);
	}
}
