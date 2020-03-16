package com.carlosvin.covid.controllers;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

public class CovidCountryDto {
	private final Map<ZonedDateTime, CovidDateEntryDto> dates;
	public final int confirmed;
	public final int deaths;
	
	public CovidCountryDto(Iterable<CovidDateEntryDto> entries, int confirmed, int deaths) {
		this.dates = new HashMap<ZonedDateTime, CovidDateEntryDto>();
		this.confirmed = confirmed;
		this.deaths = deaths;
		for (CovidDateEntryDto e: entries) {
			dates.put(e.getDate(), e);
		}
	}
}