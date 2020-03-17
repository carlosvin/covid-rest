package com.carlosvin.covid.services;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import javax.validation.constraints.Size;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.carlosvin.covid.models.CountryStats;
import com.carlosvin.covid.models.DateStats;
import com.carlosvin.covid.repositories.CovidDataRepository;

@Service
public class CovidEntriesServiceImpl implements CovidEntriesService {
	
	private static final long TTL_MS = 3600000;
	private long lastSaved;

	private final CovidDataRepository repo;
	private final DataSource source;
	private static final Logger LOG = LoggerFactory.getLogger(CovidEntriesServiceImpl.class);

	
	@Autowired
	public CovidEntriesServiceImpl(CovidDataRepository repo, DataSource source) {
		this.repo = repo;
		this.source = source;
		this.lastSaved = 0;
	}
	
	private boolean isOutDated () {
		return (System.currentTimeMillis() - lastSaved) > TTL_MS;
	}
	
	private void load() throws NotFoundException {
		if (isOutDated()) {
			try {
				repo.init(source.fetchData());
				lastSaved = System.currentTimeMillis();
				LOG.info("Data fetched and loaded into the repository");
			} catch (IOException e) {
				throw new NotFoundException(e.getMessage());
			}
		}
	}

	@Override
	public Iterable<DateStats> getEntries(@Size(max = 2, min = 2) String countryCode) throws NotFoundException {
		load();
		/*Iterable<DateStats> entries = repo.getEntries(countryCode);
		if (entries == null) {
			throw new NotFoundException("No entries for country " + countryCode);
		}
		return entries;*/
		return null;
	}

	@Override
	public DateStats getCountry(@Size(max = 2, min = 2) String countryCode, ZonedDateTime date) throws NotFoundException {
		load();
		DateStats stats = repo.getStats(countryCode, date);
		if (stats == null) {
			throw new NotFoundException("There are no stats for country " + countryCode + " in date " + date);
		}
		return stats;
	}

	@Override
	public Stream<? extends CountryStats> getCountries() throws NotFoundException {
		load();
		return StreamSupport.stream(repo.getCountries().spliterator(), false);
	}

	@Override
	public CountryStats getCountry(String countryCode) throws NotFoundException {
		load();
		return repo.getAggregateStats(countryCode);
	}

	@Override
	public Stream<? extends DateStats> getDatesByCountry(String country) throws NotFoundException {
		load();
		return StreamSupport.stream(repo.getStats(country).spliterator(), false);
	}
}
