package com.carlosvin.covid.services;

import java.time.ZonedDateTime;
import java.util.stream.Stream;

import com.carlosvin.covid.models.CountryStats;
import com.carlosvin.covid.models.DateStats;
import com.carlosvin.covid.services.exceptions.NotFoundException;

public interface CovidEntriesService {
	
	DateStats getCountry(String countryCode, ZonedDateTime date) throws NotFoundException;
	
	CountryStats getCountry(String countryCode) throws NotFoundException;

	Stream<? extends CountryStats> getCountries() throws NotFoundException;

	Stream<? extends DateStats> getDatesByCountry(String country) throws NotFoundException;

	Stream<? extends DateStats> getDates() throws NotFoundException;
	
	DateStats getDate(ZonedDateTime convert)throws NotFoundException;
	
	Stream<? extends CountryStats> getCountries(ZonedDateTime date) throws NotFoundException;

}
