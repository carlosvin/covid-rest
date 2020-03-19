package com.carlosvin.covid.services;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CountriesProvider {

	private static final Map<String, String> countries = new HashMap<>();
	
	static {
		for (String iso : Locale.getISOCountries()) {
			Locale l = new Locale("EN", iso);
			countries.put(l.getDisplayCountry(), iso);
		}
		// for some reason input data has this weird format for KR
		countries.put("Korea, South", "KR");
	}
	
	/**
	 * @param country name
	 * @return Country code; it will return null if country name is not found.
	 * */
	public static String get(String countryName) {
		return countries.get(countryName);
	}
}
