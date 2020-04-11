package com.carlosvin.covid.models;

import java.time.LocalDate;

public interface DateStats extends Stats, Identifiable<LocalDate> {
	
	LocalDate getDate();
	
	@Override
	default LocalDate getId() {
		return getDate();
	}	
}
