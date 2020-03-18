package com.carlosvin.covid.services;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.carlosvin.covid.models.CountryStats;
import com.carlosvin.covid.models.DateStats;
import com.carlosvin.covid.repositories.CovidDataRepository;
import com.carlosvin.covid.services.exceptions.NotFoundException;

@Service
public class CovidEntriesServiceImpl implements CovidEntriesService {
	
	private long lastSaved;

	private final CovidDataRepository repo;
	private final DataSource source;
	private static final Logger LOG = LoggerFactory.getLogger(CovidEntriesServiceImpl.class);
	
	@Autowired
	public CovidEntriesServiceImpl(CovidDataRepository repo, DataSource source) {
		this.repo = repo;
		this.source = source;
		this.lastSaved = 0;
		this.load();
	}
	
	@Scheduled(fixedDelayString = "${fixedDelay.ms:3600000}", initialDelayString = "${fixedDelay.ms:3600000}")
	private void scheduledLoad() {
		this.load();
	}
	
	private void load() {
		try {
			repo.init(source.fetchData());
			lastSaved = System.currentTimeMillis();
			LOG.info("Data fetched and loaded into the repository");
		} catch (IOException e) {
			String msg = "Problem refreshing repository information";
			LOG.warn(msg, e);
			if (lastSaved == 0) {
				LOG.warn(e.getMessage());
			}
		}
	}

	@Override
	public Stream<? extends CountryStats> getCountries() {
		return StreamSupport.stream(repo.getCountries().spliterator(), false);
	}

	@Override
	public CountryStats getCountry(String countryCode) throws NotFoundException {
		CountryStats c = repo.getAggregateStats(countryCode);
		if (c == null) {
			throw new NotFoundException("Country not found " + countryCode);
		}
		return repo.getAggregateStats(countryCode);
	}

	@Override
	public Stream<? extends DateStats> getDatesByCountry(String country) throws NotFoundException {
		return StreamSupport.stream(repo.getStats(country).spliterator(), false);
	}

	@Override
	public Stream<? extends DateStats> getDates() throws NotFoundException {
		return repo.getDates();
	}

	@Override
	public DateStats getDate(ZonedDateTime date) throws NotFoundException {
		DateStats stats = repo.getAggregateStats(date);
		if (stats == null) {
			throw new NotFoundException("There are no stats on " + date);
		}
		return stats;
	}

	@Override
	public Stream<? extends CountryStats> getCountries(ZonedDateTime date) throws NotFoundException {
		return repo.getStats(date);
	}

	@Override
	public CountryStats getCountry(ZonedDateTime date, String countryCode) throws NotFoundException {
		CountryStats stats = repo.getStats(date, countryCode);
		if (stats == null) {
			throw new NotFoundException("There are no stats for country " + countryCode + " in date " + date);
		}
		return stats;
	}

	@Override
	public DateStats getDate(String countryCode, ZonedDateTime date) throws NotFoundException {
		DateStats stats = repo.getStats(countryCode, date);
		if (stats == null) {
			throw new NotFoundException("There are no stats for country " + countryCode + " in date " + date);
		}
		return stats;
	}
}
