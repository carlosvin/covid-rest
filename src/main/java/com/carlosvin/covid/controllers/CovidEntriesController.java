package com.carlosvin.covid.controllers;

import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.carlosvin.covid.models.CountryStats;
import com.carlosvin.covid.models.DateStats;
import com.carlosvin.covid.services.CovidEntriesService;
import com.carlosvin.covid.services.DateUtils;
import com.carlosvin.covid.services.NotFoundException;

@RestController
public class CovidEntriesController {
	
	private final CovidEntriesService service;
	
	@Autowired
	public CovidEntriesController(CovidEntriesService service) {
		this.service = service;
	}
	
	@GetMapping("/countries")
	public Stream<CountryStatsDto> getCountries() throws NotFoundException {
		return this.service.getCountries().map(c -> new CountryStatsDto(c));
	}
	
	@GetMapping("countries/{country}")
	public CountryStats getTasks(@PathVariable String country) throws NotFoundException{
		return null;
		//return this.service.getEntries(country);
	}
	
	@GetMapping("countries/{country}/dates/{date}")
	public DateStats getTask(@PathVariable String country, @PathVariable long epochDay) throws NotFoundException{
		return this.service.getEntry(country, DateUtils.convert(epochDay));
	}
	
}
