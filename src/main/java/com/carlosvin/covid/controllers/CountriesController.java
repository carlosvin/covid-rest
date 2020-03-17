package com.carlosvin.covid.controllers;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.carlosvin.covid.controllers.dtos.CountryStatsDto;
import com.carlosvin.covid.controllers.dtos.DateStatsDto;
import com.carlosvin.covid.services.CovidEntriesService;
import com.carlosvin.covid.services.DateUtils;
import com.carlosvin.covid.services.NotFoundException;

@RestController
public class CountriesController {
	
	private final CovidEntriesService service;
	
	@Autowired
	public CountriesController(CovidEntriesService service) {
		this.service = service;
	}
	
	@GetMapping("/countries")
	public Stream<CountryStatsDto> getCountries() throws NotFoundException {
		return this.service.getCountries().map(c -> new CountryStatsDto(c));
	}
	
	@GetMapping("/countries/{country}")
	public CountryStatsDto getCountry(@Size(min=2, max=2) @PathVariable String country) throws NotFoundException{
		return new CountryStatsDto(service.getCountry(country));
	}
	
	@GetMapping("/countries/{country}/dates")
	public Map<Long, DateStatsDto> getDatesByCountry(@Size(min=2, max=2) @PathVariable String country) throws NotFoundException{
		return service.getDatesByCountry(country)
				.map(d -> new DateStatsDto(d))
				.collect(Collectors.toMap(d -> ((DateStatsDto)d).epochDays, d -> (DateStatsDto)d));
	}
	
	@GetMapping("/countries/{country}/dates/{epochDays}")
	public DateStatsDto getDateByCountry(@Size(min=2, max=2) @PathVariable String country, @PathVariable long epochDays) throws NotFoundException{
		return new DateStatsDto(service.getCountry(country, DateUtils.convert(epochDays)));
	}
	
}
