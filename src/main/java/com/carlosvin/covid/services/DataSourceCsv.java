package com.carlosvin.covid.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.carlosvin.covid.models.CovidDataEntry;
import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;

// TODO Maybe it is worthy reading from https://www.ecdc.europa.eu/sites/default/files/documents/COVID-19-geographic-disbtribution-worldwide-2020-03-15.xls
// It is more official and data model is more consistent

@Service
public class DataSourceCsv implements DataSource {
	private static final DateTimeFormatter DF = DateTimeFormatter.ofPattern("MM-dd-yyyy");
	private static final String URL_PREFIX = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_daily_reports/";
	private LocalDate date;
	
	private URL getUrl() throws MalformedURLException {
		date = LocalDate.now(); //.minusDays(1);
		return new URL(
				String.format(
						URL_PREFIX + 
						DF.format(date) + ".csv"));
	}

	@Override
	public Stream<CovidDataEntry> fetchData() throws IOException {
		URL url;
		try {
			url = getUrl();
		} catch (MalformedURLException e1) {
			// This should not happen
			throw new RuntimeException(e1);
		}
		HeaderColumnNameMappingStrategy<CovidDataEntryCsv> ms = new HeaderColumnNameMappingStrategy<CovidDataEntryCsv>();
	    ms.setType(CovidDataEntryCsv.class);
	    
		try (BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()))) {
			CsvToBean<CovidDataEntryCsv> reader = new CsvToBeanBuilder<CovidDataEntryCsv>(new CSVReader(in))
					.withMappingStrategy(ms)
					.withType(CovidDataEntryCsv.class)
					.build();
			long epoch = date.atStartOfDay(ZoneId.systemDefault()).toEpochSecond();
			return reader
					.stream()
					.map(c -> (CovidDataEntry)c.setDate(epoch));
		}
	}

}
