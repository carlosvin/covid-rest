package com.carlosvin.covid.models;

import java.time.ZonedDateTime;

public interface DateStats extends Stats, Identifiable<ZonedDateTime> {
	
	ZonedDateTime getDate();
	
	@Override
	default ZonedDateTime getId() {
		return getDate();
	}
	
}
