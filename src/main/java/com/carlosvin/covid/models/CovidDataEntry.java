package com.carlosvin.covid.models;

import javax.validation.constraints.NotBlank;

public interface CovidDataEntry {
	public long getDate();

	@NotBlank
	public String getCountry();
	@NotBlank
	public String getCountryCode();

	public int getConfirmed();

	public int getNewDeaths();
	
}
