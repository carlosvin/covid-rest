package com.carlosvin.covid.repositories;

import java.time.LocalDate;
import java.util.stream.Stream;

import com.carlosvin.covid.models.CountryStats;
import com.carlosvin.covid.models.DateCountryStats;
import com.carlosvin.covid.models.DateStats;

public interface CovidDataRepository {

	/**
	 * Initialises repository data
	 * */
	void init(Stream<DateCountryStats> fetchData) throws InitializationException;
	
	/**
	 * @return Country statistics aggregate
	 * */
	CountryStats getAggregateStats(String countryCode);
	
	/**
	 * @return Country statistics aggregate
	 * */
	DateStats getAggregateStats(LocalDate date);
	
	/**
	 * @return Statistics for a country in a date
	 * */
	DateStats getStats(String countryCode, LocalDate date);
	
	/**
	 * @return Statistics for a date in a country
	 * */
	CountryStats getStats(LocalDate date, String countryCode);
	
	/**
	 * @return All statistics by country on that date
	 * */
	Stream<CountryStats> getStats(LocalDate date);
	
	/**
	 * @return All date statistics for a country
	 * */
	Iterable<DateStats> getStats(String countryCode);

	/**
	 * @return all countries statistics
	 * */
	Iterable<? extends CountryStats> getCountries();

	Stream<? extends DateStats> getDates();

}