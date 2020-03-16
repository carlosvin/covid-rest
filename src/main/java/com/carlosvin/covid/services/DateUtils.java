package com.carlosvin.covid.services;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

public class DateUtils {
	public static ZonedDateTime convert (Date date) {		
		return convert(date.getTime() / 1000 / 3600 / 24);
	}
	
	
	public static ZonedDateTime convert (long epochDay) {		
		return LocalDate
			.ofEpochDay(epochDay)
		    .atTime(0, 0, 0)
		    .atZone(ZoneId.of("UTC"));
	}
}
