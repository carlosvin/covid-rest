package com.carlosvin.covid.repositories;

import java.time.ZonedDateTime;
import java.util.TreeMap;

import com.carlosvin.covid.models.Country;
import com.carlosvin.covid.models.CountryStats;
import com.carlosvin.covid.models.DateCountryStats;
import com.carlosvin.covid.models.DateStats;
import com.carlosvin.covid.models.Stats;

public class CountryModel implements CountryStats {

	private final Country country;
	private final TreeMap<ZonedDateTime, DateStats> datesRepo;
	private int confirmed, deaths;

	public CountryModel(Country country) {
		this.country = country;
		this.datesRepo = new TreeMap<>();
		this.confirmed = this.deaths = 0;
	}

	public CountryModel(Country country, Stats ... stats) {
		this(country);
		for (Stats s: stats) {
			this.confirmed += s.getConfirmed();
			this.deaths += s.getDeaths();
		}
	}

	public void add(DateCountryStats c) {
		DateStats dateStats = c.getDateStats();
		Stats saved = datesRepo.get(dateStats.getId());
		if (saved == null) {
			datesRepo.put(dateStats.getId(), dateStats);
		} else {
			datesRepo.put(dateStats.getId(), new DateModel(dateStats, saved));
		}
		confirmed += dateStats.getConfirmed();
		deaths += dateStats.getDeaths();
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
	public String getCountry() {
		return country.getCountry();
	}

	@Override
	public String getCode() {
		return country.getCode();
	}

	public DateStats get(ZonedDateTime date) {
		return datesRepo.get(date);
	}

	public Iterable<DateStats> getDates() {
		return datesRepo.values();
	}
}
