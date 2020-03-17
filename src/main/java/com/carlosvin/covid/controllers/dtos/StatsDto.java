package com.carlosvin.covid.controllers.dtos;

import com.carlosvin.covid.models.Stats;

public class StatsDto {

	public final int confirmedCases;
	public final int deathsNumber;
	
	public StatsDto (Stats s) {
		this.confirmedCases = s.getConfirmed();
		this.deathsNumber = s.getDeaths();
	}
}
