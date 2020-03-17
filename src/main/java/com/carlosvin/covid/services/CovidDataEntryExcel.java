package com.carlosvin.covid.services;

import java.time.ZonedDateTime;

import org.apache.poi.ss.usermodel.Row;

import com.carlosvin.covid.models.Country;
import com.carlosvin.covid.models.DateCountryStats;
import com.carlosvin.covid.models.DateStats;

enum Cells {
	Date(0), Cases(4), NewDeaths(5), Country(6), CountryCode(7);

	private final int value;

	private Cells(final int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
}

public class CovidDataEntryExcel implements DateCountryStats {

	static class DateEntryExcel implements DateStats {
		private final ZonedDateTime date;
		private final int newConfCases;
		private final int newDeaths;

		public DateEntryExcel(Row r) {
			date = DateUtils.convert(r.getCell(Cells.Date.getValue()).getDateCellValue());
			newConfCases = (int) r.getCell(Cells.Cases.getValue()).getNumericCellValue();
			newDeaths = (int) r.getCell(Cells.NewDeaths.getValue()).getNumericCellValue();
		}

		@Override
		public int getConfirmed() {
			return newConfCases;
		}

		@Override
		public int getDeaths() {
			return newDeaths;
		}

		@Override
		public ZonedDateTime getDate() {
			return date;
		}
	}

	static class CountryEntryExcel implements Country {

		private final String country;
		private final String countryCode;

		public CountryEntryExcel(Row r) {
			country = r.getCell(Cells.Country.getValue()).getStringCellValue();
			countryCode = r.getCell(Cells.CountryCode.getValue()).getStringCellValue();
		}

		@Override
		public String getCountry() {
			return country;
		}

		@Override
		public String getCode() {
			return countryCode;
		}

	}

	private final CountryEntryExcel country;
	private final DateEntryExcel date;

	public CovidDataEntryExcel(Row r) {
		date = new DateEntryExcel(r);
		country = new CountryEntryExcel(r);
	}

	@Override
	public DateStats getDateStats() {
		return date;
	}

	@Override
	public Country getCountry() {
		return country;
	}

}
