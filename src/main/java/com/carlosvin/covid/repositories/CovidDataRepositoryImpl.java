package com.carlosvin.covid.repositories;

import java.time.LocalDate;
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
	public void init(Stream<DateCountryStats> fetchData) throws InitializationException {
		CountryRepo countriesTmp = new CountryRepo();
		DateRepo datesTmp = new DateRepo();

		fetchData
			.filter(c -> c != null)
			.peek(c -> countriesTmp.add(c))
			.forEach(c -> datesTmp.add(c));
		if (countriesTmp.isEmpty() || datesTmp.isEmpty()) {
			throw new InitializationException();
		}
		countries = countriesTmp;
		dates = datesTmp;
	}

	@Override
	public CountryStats getAggregateStats(String countryCode) {
		return countries.get(countryCode);
	}

	@Override
	public DateStats getAggregateStats(LocalDate date) {
		return dates.get(date);
	}

	@Override
	public DateStats getStats(String countryCode, LocalDate date) {
		return countries.get(countryCode, date);
	}

	@Override
	public CountryStats getStats(LocalDate date, String countryCode) {
		return dates.get(date, countryCode);
	}

	@Override
	public Stream<CountryStats> getStats(LocalDate date) {
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
