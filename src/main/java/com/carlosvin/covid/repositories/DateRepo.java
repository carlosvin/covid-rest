package com.carlosvin.covid.repositories;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Stream;

import com.carlosvin.covid.models.CountryStats;
import com.carlosvin.covid.models.DateCountryStats;
import com.carlosvin.covid.models.DateStats;
import com.carlosvin.covid.models.Stats;

public class DateRepo implements Stats {
	
	SortedMap<ZonedDateTime, DateModel> dates = new TreeMap<>();
	private int confirmed = 0, deaths = 0;
	
	public void add(DateCountryStats c) {
		DateStats dateStats = c.getDateStats();
		ZonedDateTime id = dateStats.getDate();
		DateModel date = dates.get(id);
		if (date == null) {
			date = new DateModel(id);
			dates.put(id, date);
		}
		date.add(c);
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

	public DateStats get(ZonedDateTime date) {
		return dates.get(date);
	}

	public Stream<CountryStats> getCountries(ZonedDateTime date) {
		DateModel d = dates.get(date);
		if (d != null) {
			return d.getCountries();
		}
		return Stream.empty();
	}

	public Stream<? extends DateStats> get() {
		return dates.values().stream();
	}

	public CountryStats get(ZonedDateTime date, String countryCode) {
		DateModel d = dates.get(date);
		if (d != null) {
			return d.get(countryCode);
		}
		return null;
	}

}
