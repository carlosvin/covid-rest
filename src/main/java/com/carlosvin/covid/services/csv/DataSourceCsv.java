package com.carlosvin.covid.services.csv;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Clock;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import com.carlosvin.covid.models.DateCountryStats;
import com.carlosvin.covid.services.DataSource;
import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;

@Service
@PropertySource("classpath:application.properties")
public class DataSourceCsv implements DataSource {
	private static final Logger LOG = LoggerFactory.getLogger(DataSourceCsv.class);
	private static final DateTimeFormatter DF = DateTimeFormatter.ofPattern("MM-dd-yyyy");
	private final String baseUrl;
	private final Clock clock;
	private LocalDate date;

	@Autowired
	public DataSourceCsv(@Value("${base.url.csv}") String baseUrl, Clock clock) {
		this.baseUrl = baseUrl;
		this.clock = clock;
	}
	
	@Override
	public URL getUrl(int daysToSubtract) {
		date = LocalDate.now();
		String urlStr = String.format(baseUrl + DF.format(LocalDate.now(clock).minusDays(daysToSubtract)) + ".csv");
		try {
			return new URL(urlStr);
		} catch (MalformedURLException e) {
			return getClass().getClassLoader().getResource(urlStr);
		}
	}

	@Override
	public Stream<DateCountryStats> fetchData(int minusDays) throws IOException {
		URL url = getUrl(minusDays);
		LOG.info("Fetching CSV file: {}", url);

		HeaderColumnNameMappingStrategy<CovidDataEntryCsv> ms = new HeaderColumnNameMappingStrategy<CovidDataEntryCsv>();
	    ms.setType(CovidDataEntryCsv.class);
	    
		try (BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()))) {
			CsvToBean<CovidDataEntryCsv> reader = new CsvToBeanBuilder<CovidDataEntryCsv>(new CSVReader(in))
					.withMappingStrategy(ms)
					.withType(CovidDataEntryCsv.class)
					.build();
			long epochDays = date.toEpochDay();
			return reader
					.stream()
					.map(c -> (CovidDataEntryCsv)c.setDate(epochDays));
		}
	}

	@Override
	public Stream<DateCountryStats> fetchData() throws IOException {
		return fetchData(0);
	}

}
