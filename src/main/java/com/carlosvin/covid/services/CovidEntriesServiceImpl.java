package com.carlosvin.covid.services;

import java.io.IOException;
import java.time.LocalDate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.carlosvin.covid.models.CountryStats;
import com.carlosvin.covid.models.DateCountryStats;
import com.carlosvin.covid.models.DateStats;
import com.carlosvin.covid.repositories.CovidDataRepository;
import com.carlosvin.covid.repositories.InitializationException;
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
		this.firstLoad();
	}

	@Scheduled(fixedDelayString = "${fixedDelay.ms:3600000}", initialDelayString = "${fixedDelay.ms:3600000}")
	private void scheduledLoad() {
		try {
			this.load(source.fetchData());
		} catch (IOException|InitializationException e) {
			String msg = "Problem refreshing repository information";
			LOG.warn(msg, e);
			if (lastSaved == 0) {
				LOG.warn(e.getMessage());
			}
		}
	}

	private void firstLoad() {
		try {
			this.load(source.fetchData());
		} catch (IOException|InitializationException e) {
			try {
				LOG.info("Today's data is not yet available, loading yesterday's: {}", e.getMessage());
				this.load(source.fetchData(1));
			} catch (IOException|InitializationException e1) {
				LOG.warn(e.getMessage());
			}
		}
	}

	private void load(Stream<DateCountryStats> data) throws InitializationException {
		repo.init(data);
		lastSaved = System.currentTimeMillis();
		LOG.info("Data fetched and loaded into the repository");		
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
	public DateStats getDate(LocalDate date) throws NotFoundException {
		DateStats stats = repo.getAggregateStats(date);
		if (stats == null) {
			throw new NotFoundException("There are no stats on " + date);
		}
		return stats;
	}

	@Override
	public Stream<? extends CountryStats> getCountries(LocalDate date) throws NotFoundException {
		return repo.getStats(date);
	}

	@Override
	public CountryStats getCountry(LocalDate date, String countryCode) throws NotFoundException {
		CountryStats stats = repo.getStats(date, countryCode);
		if (stats == null) {
			throw new NotFoundException("There are no stats for country " + countryCode + " in date " + date);
		}
		return stats;
	}

	@Override
	public DateStats getDate(String countryCode, LocalDate date) throws NotFoundException {
		DateStats stats = repo.getStats(countryCode, date);
		if (stats == null) {
			throw new NotFoundException("There are no stats for country " + countryCode + " in date " + date);
		}
		return stats;
	}
}
