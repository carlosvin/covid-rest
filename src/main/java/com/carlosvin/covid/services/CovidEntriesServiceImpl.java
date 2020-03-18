package com.carlosvin.covid.services;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.carlosvin.covid.models.CountryStats;
import com.carlosvin.covid.models.DateStats;
import com.carlosvin.covid.repositories.CovidDataRepository;
import com.carlosvin.covid.services.exceptions.NotFoundException;

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
				if (lastSaved > 0) {
					LOG.warn("Problem refreshing repository information ", e);
				} else {
					throw new NotFoundException(e.getMessage());
				}
			}
		}
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

	@Override
	public Stream<? extends DateStats> getDates() throws NotFoundException {
		load();
		return repo.getDates();
	}

	@Override
	public DateStats getDate(ZonedDateTime date) throws NotFoundException {
		load();
		DateStats stats = repo.getAggregateStats(date);
		if (stats == null) {
			throw new NotFoundException("There are no stats on " + date);
		}
		return stats;
	}

	@Override
	public Stream<? extends CountryStats> getCountries(ZonedDateTime date) throws NotFoundException {
		load();
		return repo.getStats(date);
	}

	@Override
	public CountryStats getCountry(ZonedDateTime date, String countryCode) throws NotFoundException {
		load();
		CountryStats stats = repo.getStats(date, countryCode);
		if (stats == null) {
			throw new NotFoundException("There are no stats for country " + countryCode + " in date " + date);
		}
		return stats;
	}

	@Override
	public DateStats getDate(String countryCode, ZonedDateTime date) throws NotFoundException {
		load();
		DateStats stats = repo.getStats(countryCode, date);
		if (stats == null) {
			throw new NotFoundException("There are no stats for country " + countryCode + " in date " + date);
		}
		return stats;
	}
}
