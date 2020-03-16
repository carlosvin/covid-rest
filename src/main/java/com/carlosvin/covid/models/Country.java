package com.carlosvin.covid.models;

public interface Country extends Identifiable<String> {
	
	String getCountry();
	String getCode();
	
	@Override
	default String getId() {
		return getCode();
	}
}
