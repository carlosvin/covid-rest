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
@RequestMapping("/dates")
public class DatesController {

	private final CovidEntriesService service;

	@Autowired
	public DatesController(CovidEntriesService service) {
		this.service = service;
	}

	@GetMapping
	public SortedMap<String, DateStatsDto> getDates(HttpServletRequest request) throws NotFoundException {
		TreeMap <String, DateStatsDto> res = new TreeMap<>();
		service.getDates()
			.map(d -> new DateStatsDto.WithUrl(d, request.getRequestURI()))
			.forEach(d -> res.put(d.date.toString(), d));
		return res;
	}

	@GetMapping("/{isoDateStr}")
	public DateStatsDto getDate(@PathVariable @Size(min = 10, max = 20) String isoDateStr) throws NotFoundException {
		return new DateStatsDto(service.getDate(DateUtils.convert(isoDateStr)));
	}

	@GetMapping("/{isoDateStr}/countries")
	public SortedMap<String, CountryStatsDto> getCountries(HttpServletRequest request, @Size(min = 10, max = 20) @PathVariable String isoDateStr)
			throws NotFoundException {
		TreeMap<String, CountryStatsDto> res = new TreeMap<>();
		service
			.getCountries(DateUtils.convert(isoDateStr))
			.map(c -> new CountryStatsDto.WithUrl(c, request.getRequestURI()))
			.forEach(c -> res.put(c.countryCode, c));
		return res;
	}

	@GetMapping("/{isoDateStr}/countries/{country}")
	public CountryStatsDto getDateByCountry(@Size(min = 10, max = 20) @PathVariable String isoDateStr,
			@Size(min = 2, max = 2) @PathVariable String country) throws NotFoundException {
		return new CountryStatsDto(service.getCountry(DateUtils.convert(isoDateStr), country));
	}

}
