package com.carlosvin.covid.services;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Date;

public class DateUtils {
	public static final DateTimeFormatter DF = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	public static ZonedDateTime convert (Date date) {		
		return convert(date.getTime() / 1000 / 3600 / 24);
	}
	
	
	public static ZonedDateTime convert (long epochDay) {		
		return LocalDate
			.ofEpochDay(epochDay)
		    .atTime(0, 0, 0)
		    .atZone(ZoneId.of("UTC"));
	}


	public static ZonedDateTime convert(Long epochDays, String dateIsoStr) {
		if (epochDays == null) {
			return convert(dateIsoStr);
		} else {
			return convert(epochDays);
		}
	}

	public static ZonedDateTime convert(String dateIsoStr) {
		TemporalAccessor w = DF.parse(dateIsoStr.substring(0, 10));
		return LocalDate
			.from(w)
		    .atTime(0, 0, 0)
		    .atZone(ZoneId.of("UTC"));
	}

	public static String toIsoStr(ZonedDateTime date) {
		return DF.format(date);
	}
}
