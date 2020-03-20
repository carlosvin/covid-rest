package com.carlosvin.covid.controllers;

import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.carlosvin.covid.controllers.dtos.CountryStatsDto;
import com.carlosvin.covid.controllers.dtos.DateStatsDto;
import com.carlosvin.covid.services.CovidEntriesService;
import com.carlosvin.covid.services.DateUtils;
import com.carlosvin.covid.services.exceptions.NotFoundException;

@Validated
@RestController
public class DatesController {

	private final CovidEntriesService service;

	@Autowired
	public DatesController(CovidEntriesService service) {
		this.service = service;
	}

	@GetMapping("/dates")
	public Map<String, DateStatsDto> getDates(HttpServletRequest request) throws NotFoundException {
		return service.getDates().map(d -> new DateStatsDto.WithUrl(d, request.getRequestURI()))
				.collect(Collectors.toMap(d -> ((DateStatsDto) d).date, d -> (DateStatsDto) d));
	}

	@GetMapping("/dates/{isoDateStr}")
	public DateStatsDto getDate(@PathVariable @Size(min = 10, max = 20) String isoDateStr) throws NotFoundException {
		return new DateStatsDto(service.getDate(DateUtils.convert(isoDateStr)));
	}

	@GetMapping("/dates/{isoDateStr}/countries")
	public Map<String, CountryStatsDto> getCountries(@Size(min = 10, max = 20) @PathVariable String isoDateStr)
			throws NotFoundException {
		return service.getCountries(DateUtils.convert(isoDateStr)).map(c -> new CountryStatsDto(c))
				.collect(Collectors.toMap(c -> ((CountryStatsDto) c).countryCode, c -> (CountryStatsDto) c));

	}

	@GetMapping("/dates/{isoDateStr}/countries/{country}")
	public CountryStatsDto getDateByCountry(@Size(min = 10, max = 20) @PathVariable String isoDateStr,
			@Size(min = 2, max = 2) @PathVariable String country) throws NotFoundException {
		return new CountryStatsDto(service.getCountry(DateUtils.convert(isoDateStr), country));
	}

}
