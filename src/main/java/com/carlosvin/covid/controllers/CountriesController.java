package com.carlosvin.covid.controllers;

import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.carlosvin.covid.controllers.dtos.CountryStatsDto;
import com.carlosvin.covid.controllers.dtos.DateStatsDto;
import com.carlosvin.covid.services.CovidEntriesService;
import com.carlosvin.covid.services.DateUtils;
import com.carlosvin.covid.services.exceptions.NotFoundException;

@Validated
@RestController
@RequestMapping("/countries")
public class CountriesController {

	private final CovidEntriesService service;

	@Autowired
	public CountriesController(CovidEntriesService service) {
		this.service = service;
	}

	@GetMapping("")
	public SortedMap<String, CountryStatsDto> getCountries(HttpServletRequest request) throws NotFoundException {
		TreeMap<String, CountryStatsDto> res = new TreeMap<>();
		service
			.getCountries()
			.map(c -> new CountryStatsDto.WithUrl(c, request.getRequestURI()))
			.forEach(d -> res.put(d.countryCode, d));
		return res;
	}

	@GetMapping("/{country}")
	public CountryStatsDto getCountry(@Size(min = 2, max = 2) @PathVariable String country) throws NotFoundException {
		return new CountryStatsDto(service.getCountry(country));
	}

	@GetMapping("/{country}/dates")
	public SortedMap<String, DateStatsDto> getDatesByCountry(HttpServletRequest request,
			@Size(min = 2, max = 2) @PathVariable String country) throws NotFoundException {
		TreeMap<String, DateStatsDto> res = new TreeMap<>();
		service
			.getDatesByCountry(country)
			.map(d -> new DateStatsDto.WithUrl(d, request.getRequestURI()))
			.forEach(d-> res.put(d.date.toString(), d));
		return res;
	}

	@GetMapping("/{country}/dates/{isoDateStr}")
	public DateStatsDto getDateByCountry(@Size(min = 2, max = 2) @PathVariable String country,
			@Size(min = 10, max = 20) @PathVariable String isoDateStr) throws NotFoundException {
		return new DateStatsDto(service.getDate(country, DateUtils.convert(isoDateStr)));
	}

}
