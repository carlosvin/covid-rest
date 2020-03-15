package com.carlosvin.covid.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.carlosvin.covid.models.CovidDataEntry;
import com.carlosvin.covid.services.CovidEntriesService;
import com.carlosvin.covid.services.NotFoundException;

@RestController
public class CovidEntriesController {
	
	private final CovidEntriesService service;
	
	@Autowired
	public CovidEntriesController(CovidEntriesService service) {
		this.service = service;
	}
	
	@GetMapping("countries/{country}")
	public Iterable<CovidDataEntry> getTasks(@PathVariable String country) throws NotFoundException{
		return this.service.getEntries(country);
	}
	
	@GetMapping("countries/{country}/dates/{date}")
	public CovidDataEntry getTask(@PathVariable String country, @PathVariable long date) throws NotFoundException{
		return this.service.getEntry(country, date);
	}
	
}
