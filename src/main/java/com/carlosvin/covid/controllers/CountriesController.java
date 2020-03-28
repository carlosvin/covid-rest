package com.carlosvin.covid.controllers;

import java.util.Map;
import java.util.stream.Collectors;

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
	public Map<String, CountryStatsDto> getCountries() throws NotFoundException {
		return this.service.getCountries()
				.map(c -> new CountryStatsDto(c))
				.collect(Collectors.toMap(
						c -> c.countryCode, c -> c ));
	}

	@GetMapping("/{country}")
	public CountryStatsDto getCountry(@Size(min = 2, max = 2) @PathVariable String country) throws NotFoundException {
		return new CountryStatsDto(service.getCountry(country));
	}

	@GetMapping("/{country}/dates")
	public Map<String, DateStatsDto> getDatesByCountry(HttpServletRequest request,
			@Size(min = 2, max = 2) @PathVariable String country) throws NotFoundException {
		return service.getDatesByCountry(country).map(d -> new DateStatsDto.WithUrl(d, request.getRequestURI()))
				.collect(Collectors.toMap(d -> ((DateStatsDto) d).date, d -> (DateStatsDto) d));
	}

	@GetMapping("/{country}/dates/{isoDateStr}")
	public DateStatsDto getDateByCountry(@Size(min = 2, max = 2) @PathVariable String country,
			@Size(min = 10, max = 20) @PathVariable String isoDateStr) throws NotFoundException {
		return new DateStatsDto(service.getDate(country, DateUtils.convert(isoDateStr)));
	}

}
