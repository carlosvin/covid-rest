package com.carlosvin.covid.services;

import javax.validation.constraints.Size;

import com.carlosvin.covid.models.CovidDataEntry;

public interface CovidEntriesService {
	Iterable<CovidDataEntry> getEntries(@Size(max = 2, min= 2) String countryCode) throws NotFoundException;
	CovidDataEntry getEntry(@Size(max = 2, min= 2) String countryCode, long date) throws NotFoundException;

}
