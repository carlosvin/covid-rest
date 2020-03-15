package com.carlosvin.covid.repositories;

import java.util.stream.Stream;

import javax.validation.constraints.Size;

import com.carlosvin.covid.models.CovidDataEntry;

public interface CovidDataRepository {

	Iterable<CovidDataEntry> getEntries(@Size(max = 2, min= 2) String countryCode);
	CovidDataEntry getEntry(@Size(max = 2, min= 2) String countryCode, long date);
	void init(Stream<CovidDataEntry> fetchData);
}