package com.carlosvin.covid.controllers;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.carlosvin.covid.controllers.dtos.DateStatsDto;
import com.carlosvin.covid.services.CovidEntriesService;
import com.carlosvin.covid.services.exceptions.NotFoundException;

@RestController
public class DatesController {
	
	private final CovidEntriesService service;
	
	@Autowired
	public DatesController(CovidEntriesService service) {
		this.service = service;
	}
	
	@GetMapping("/dates")
	public Map<Long, DateStatsDto> getDates() throws NotFoundException{
		return service.getDates()
				.map(d -> new DateStatsDto(d))
				.collect(Collectors.toMap(d -> ((DateStatsDto)d).epochDays, d -> (DateStatsDto)d));
	}
	
}
