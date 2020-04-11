package com.carlosvin.covid.services;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Date;

public class DateUtils {
	public static final DateTimeFormatter DF = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	public static LocalDate convert (Date date) {
		return date.toInstant()
	      .atZone(ZoneId.systemDefault())
	      .toLocalDate();
	}
	
	
	public static LocalDate convert (long epochDay) {		
		return LocalDate.ofEpochDay(epochDay);
	}


	public static LocalDate convert(Long epochDays, String dateIsoStr) {
		if (epochDays == null) {
			return convert(dateIsoStr);
		} else {
			return convert(epochDays);
		}
	}

	public static LocalDate convert(String dateIsoStr) {
		TemporalAccessor w = DF.parse(dateIsoStr.substring(0, 10));
		return LocalDate.from(w);
	}

	public static String toIsoStr(ZonedDateTime date) {
		return DF.format(date);
	}
}
