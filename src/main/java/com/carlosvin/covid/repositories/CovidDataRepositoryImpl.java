package com.carlosvin.covid.repositories;

import java.time.ZonedDateTime;
import java.util.stream.Stream;

import org.springframework.stereotype.Repository;

import com.carlosvin.covid.models.CountryStats;
import com.carlosvin.covid.models.DateCountryStats;
import com.carlosvin.covid.models.DateStats;

@Repository
public class CovidDataRepositoryImpl implements CovidDataRepository {
	
	CountryRepo countries = new CountryRepo();
	DateRepo dates = new DateRepo();
	
	@Override
	public void init(Stream<DateCountryStats> fetchData) {
		CountryRepo countriesTmp = new CountryRepo();
		DateRepo datesTmp = new DateRepo();
		
		fetchData.forEach(c -> {
			countriesTmp.add(c);
			datesTmp.add(c);
		});
		
		countries = countriesTmp;
		dates = datesTmp;
	}
	
	@Override
	public CountryStats getAggregateStats(String countryCode) {
		return countries.get(countryCode);
	}

	@Override
	public DateStats getAggregateStats(ZonedDateTime date) {
		return dates.get(date);
	}

	@Override
	public DateStats getStats(String countryCode, ZonedDateTime date) {
		return countries.get(countryCode, date);
	}

	@Override
	public CountryStats getStats(ZonedDateTime date, String countryCode) {
		return dates.get(date, countryCode);
	}


	@Override
	public Stream<CountryStats> getStats(ZonedDateTime date) {
		return dates.getCountries(date);
	}

	@Override
	public Iterable<DateStats> getStats(String countryCode) {
		return countries.getDates(countryCode);
	}

	@Override
	public Iterable<? extends CountryStats> getCountries() {
		return countries.get();
	}

	@Override
	public Stream<? extends DateStats> getDates() {
		return dates.get();
	}

}
