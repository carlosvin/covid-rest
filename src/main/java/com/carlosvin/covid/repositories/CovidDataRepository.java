package com.carlosvin.covid.repositories;

import java.time.ZonedDateTime;
import java.util.stream.Stream;

import com.carlosvin.covid.models.CountryStats;
import com.carlosvin.covid.models.DateCountryStats;
import com.carlosvin.covid.models.DateStats;

public interface CovidDataRepository {

	/**
	 * Initialises repository data
	 * */
	void init(Stream<DateCountryStats> fetchData);
	
	/**
	 * @return Country statistics aggregate
	 * */
	CountryStats getAggregateStats(String countryCode);
	
	/**
	 * @return Country statistics aggregate
	 * */
	DateStats getAggregateStats(ZonedDateTime date);
	
	
	/**
	 * @return Statistics for a country in a date
	 * */
	DateStats getStats(String countryCode, ZonedDateTime date);
	
	/**
	 * @return All statistics by country on that date
	 * */
	Iterable<CountryStats> getStats(ZonedDateTime date);
	
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