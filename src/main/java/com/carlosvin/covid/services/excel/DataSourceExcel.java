package com.carlosvin.covid.services.excel;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import com.carlosvin.covid.models.DateCountryStats;
import com.carlosvin.covid.services.DataSource;
import com.monitorjbl.xlsx.StreamingReader;

@Service
@PropertySource("classpath:application.properties")
public class DataSourceExcel implements DataSource {
	private static final Logger LOG = LoggerFactory.getLogger(DataSourceExcel.class);
	
	private final String url;
	
	@Autowired
	public DataSourceExcel(@Value("${url.excel}") String url) {
		this.url = url;
	}

	public URL getUrl() {
		try {
			return new URL(url);
		} catch (MalformedURLException e) {
			var cl = getClass().getClassLoader();
			return cl.getResource(url);
		}
	}

	@Override
	public Stream<DateCountryStats> fetchData() throws IOException {
		return fetchData(0);
	}
	
	private InputStream openStream () throws IOException {
		var urlTarget = getUrl();
		LOG.info("Fetching stats from: {}", urlTarget);
		return urlTarget.openStream();
	}
	
	@Override
	public Stream<DateCountryStats> fetchData(int daysToSubtract) throws IOException {
		
		try (Workbook wb = StreamingReader.builder()
		        .rowCacheSize(300)    // number of rows to keep in memory (defaults to 10)
		        .bufferSize(4096)     // buffer size to use when reading InputStream to file (defaults to 1024)
		        .open(openStream())) {
			List<DateCountryStats> l = new LinkedList<>();
			HashSet<String> errors = new HashSet<>();
			for (Row r: wb.getSheetAt(0)) {
				try {
					l.add(new CovidDataEntryExcel(r));
				} catch (Exception e) {
					errors.add(e.getMessage());
				}
			}
			if (!errors.isEmpty()) {
				LOG.warn("Ignoring rows with invalid format: {}", String.join(", ", errors));
			}
			return l.stream();
		}
	}
}
