package com.carlosvin.covid.repositories;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.carlosvin.covid.models.CountryStats;
import com.carlosvin.covid.models.DateCountryStats;
import com.carlosvin.covid.models.DateStats;
import com.carlosvin.covid.models.Stats;

public class DateRepo implements Stats {
	
	Map<ZonedDateTime, DateModel> dates = new HashMap<>();
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

	public Iterable<CountryStats> getCountries(ZonedDateTime date) {
		DateModel d = dates.get(date);
		if (d != null) {
			return d.getCountries();
		}
		return Collections.emptyList();
	}

}
