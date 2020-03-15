package com.carlosvin.covid.services;

import java.util.Date;

import javax.validation.constraints.NotBlank;

import org.apache.poi.ss.usermodel.Row;

import com.carlosvin.covid.models.CovidDataEntry;

enum Cells {
	Date(0),
	Country(1),
	NewConfCases(2),
	NewDeaths(3),
	CountryCode(4);

	private final int value;

	private Cells(final int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
}

public class CovidDataEntryExcel implements CovidDataEntry {

	private final Date date;
	private String country;
	private String countryCode;
	private int newConfCases;
	private int newDeaths;

	public CovidDataEntryExcel(Row r) {
		date = r.getCell(Cells.Date.getValue()).getDateCellValue();
		country = r.getCell(Cells.Country.getValue()).getStringCellValue();
		countryCode = r.getCell(Cells.CountryCode.getValue()).getStringCellValue();
		newConfCases = (int)r.getCell(Cells.NewConfCases.getValue()).getNumericCellValue();
		newDeaths = (int)r.getCell(Cells.NewDeaths.getValue()).getNumericCellValue();
	}

	@Override
	public long getDate() {
		return date.getTime();
	}

	@Override
	public @NotBlank String getCountry() {
		return country;
	}

	@Override
	public int getConfirmed() {
		return newConfCases;
	}

	@Override
	public int getNewDeaths() {
		return newDeaths;
	}

	@Override
	public @NotBlank String getCountryCode() {
		return countryCode;
	}

}
