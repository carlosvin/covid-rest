package com.carlosvin.covid.services;

import java.io.IOException;
import java.sql.Date;

import javax.validation.constraints.Size;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.carlosvin.covid.models.CovidDataEntry;
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
	public Iterable<CovidDataEntry> getEntries(@Size(max = 2, min = 2) String countryCode) throws NotFoundException {
		load();
		Iterable<CovidDataEntry> entries = repo.getEntries(countryCode);
		if (entries == null) {
			throw new NotFoundException("No entries for country " + countryCode);
		}
		return entries;
	}

	@Override
	public CovidDataEntry getEntry(@Size(max = 2, min = 2) String countryCode, long date) throws NotFoundException {
		load();
		CovidDataEntry entry = repo.getEntry(countryCode, date);
		if (entry == null) {
			throw new NotFoundException("No entries for country " + countryCode + " in date " + new Date(date));
		}
		return entry;
	}
}
