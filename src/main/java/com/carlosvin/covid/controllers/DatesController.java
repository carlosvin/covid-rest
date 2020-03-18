package com.carlosvin.covid.controllers;

import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.carlosvin.covid.controllers.dtos.CountryStatsDto;
import com.carlosvin.covid.controllers.dtos.DateStatsDto;
import com.carlosvin.covid.services.CovidEntriesService;
import com.carlosvin.covid.services.DateUtils;
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
	
	@GetMapping("/dates/{epochDays}")
	public DateStatsDto getDate(@PathVariable long epochDays) throws NotFoundException{
		return new DateStatsDto(service.getDate(DateUtils.convert(epochDays)));
	}

	@GetMapping("/dates/{epochDays}/countries")
	public Map<String, CountryStatsDto> getCountries(@PathVariable long epochDays) throws NotFoundException{
		return service
				.getCountries(DateUtils.convert(epochDays))
				.map(c -> new CountryStatsDto(c))
				.collect(Collectors.toMap(c -> ((CountryStatsDto)c).countryCode, c -> (CountryStatsDto)c));

	}
	
	@GetMapping("/dates/{epochDays}/countries/{country}")
	public CountryStatsDto getDateByCountry(@PathVariable long epochDays, @Size(min=2, max=2) @PathVariable String country) throws NotFoundException{
		return new CountryStatsDto(service.getCountry(DateUtils.convert(epochDays), country));
	}
	
}
