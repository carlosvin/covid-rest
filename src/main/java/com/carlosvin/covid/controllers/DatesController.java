package com.carlosvin.covid.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.carlosvin.covid.models.DateStats;
import com.carlosvin.covid.services.CovidEntriesService;
import com.carlosvin.covid.services.DateUtils;
import com.carlosvin.covid.services.NotFoundException;

@RestController
@RequestMapping("/dates")
public class DatesController {
	
	private final CovidEntriesService service;
	
	@Autowired
	public DatesController(CovidEntriesService service) {
		this.service = service;
	}
	
	@GetMapping("/")
	public DateStats getDates(@PathVariable String country, @PathVariable long epochDay) throws NotFoundException{
		return this.service.getCountry(country, DateUtils.convert(epochDay));
	}
	
}
