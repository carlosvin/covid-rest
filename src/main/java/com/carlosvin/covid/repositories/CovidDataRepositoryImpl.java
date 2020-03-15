package com.carlosvin.covid.repositories;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import javax.validation.constraints.Size;

import org.springframework.stereotype.Repository;

import com.carlosvin.covid.controllers.CovidDateEntryDto;
import com.carlosvin.covid.models.CovidDataEntry;

@Repository
public class CovidDataRepositoryImpl implements CovidDataRepository {
	
	Map<String, Map<Long, CovidDataEntry>> byCountryByDate = new HashMap<>();

	@Override
	public Iterable<CovidDataEntry> getEntries(@Size(max = 2, min = 2) String countryCode) {
		Map<Long, CovidDataEntry> byDate = byCountryByDate.get(countryCode);
		if (byDate != null) {
			return byDate.values();
		}
		return null;
	}

	@Override
	public CovidDataEntry getEntry(@Size(max = 2, min = 2) String countryCode, long date) {
		Map<Long, CovidDataEntry> byDate = byCountryByDate.get(countryCode);
		if (byDate != null) {
			return byDate.get(date);
		}
		return null;
	}

	@Override
	public void init(Stream<CovidDataEntry> fetchData) {
		byCountryByDate.clear();
		fetchData.forEach(c -> {
			Map<Long, CovidDataEntry> byDate = byCountryByDate.get(c.getCountryCode());
			if (byDate == null) {
				byDate = new HashMap<Long, CovidDataEntry>();
				byCountryByDate.put(c.getCountryCode(), byDate);
			}
			CovidDataEntry entry = byDate.get(c.getDate());
			if (entry == null) {
				byDate.put(c.getDate(), c);
			} else {
				byDate.put(c.getDate(), add(entry, c));				
			}
		});
	}
	
	private static CovidDataEntry add (CovidDataEntry a, CovidDataEntry b) {
		return new CovidDateEntryDto(
				new Date(a.getDate()), 
				a.getCountry(), 
				a.getCountryCode(), 
				a.getConfirmed() + b.getConfirmed(), 
				a.getNewDeaths() + b.getNewDeaths());
	}


}
