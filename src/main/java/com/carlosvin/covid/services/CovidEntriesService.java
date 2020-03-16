package com.carlosvin.covid.services;

import java.time.ZonedDateTime;
import java.util.stream.Stream;

import javax.validation.constraints.Size;

import com.carlosvin.covid.models.CountryStats;
import com.carlosvin.covid.models.DateStats;

public interface CovidEntriesService {
	Iterable<DateStats> getEntries(@Size(max = 2, min= 2) String countryCode) throws NotFoundException;
	DateStats getEntry(@Size(max = 2, min= 2) String countryCode, ZonedDateTime date) throws NotFoundException;
	Stream<? extends CountryStats> getCountries() throws NotFoundException;

}
