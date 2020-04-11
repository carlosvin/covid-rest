package com.carlosvin.covid.services.excel;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Clock;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
	private static String [] EXT = {".xlsx", ".xls"};
	private static final Logger LOG = LoggerFactory.getLogger(DataSourceExcel.class);
	private static final DateTimeFormatter DF = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	
	private final String baseUrl;
	private final Clock clock;
	
	@Autowired
	public DataSourceExcel(@Value("${base.url.excel}") String baseUrl, Clock clock) throws IOException {
		this.baseUrl = baseUrl;
		this.clock = clock;
	}

	public URL getUrl(int daysToSubtract, String ext) {
		String urlStr = String.format(baseUrl + DF.format(LocalDate.now(clock).minusDays(daysToSubtract)) + ext);
		try {
			return new URL(urlStr);
		} catch (MalformedURLException e) {
			return getClass().getClassLoader().getResource(urlStr);
		}
	}

	@Override
	public Stream<DateCountryStats> fetchData() throws IOException {
		return fetchData(0);
	}
	
	private InputStream openStream (int daysToSubtract) throws IOException {
		for (String ext: EXT) {
			try {
				URL url = getUrl(daysToSubtract, ext);
				LOG.info("Fetching stats from: {}", url);
				return url.openStream();
			} catch (IOException e) {
				LOG.warn("URL not found {}", e.getMessage());
			}
		}
		throw new IOException("Cannot open stream for any of configured URLs");
	}
	
	@Override
	public Stream<DateCountryStats> fetchData(int daysToSubtract) throws IOException {
		
		try (Workbook wb = StreamingReader.builder()
		        .rowCacheSize(300)    // number of rows to keep in memory (defaults to 10)
		        .bufferSize(4096)     // buffer size to use when reading InputStream to file (defaults to 1024)
		        .open(openStream(daysToSubtract))) {
			List<DateCountryStats> l = new LinkedList<DateCountryStats>();
			HashSet<String> errors = new HashSet<String>();
			for (Row r: wb.getSheetAt(0)) {
				try {
					l.add(new CovidDataEntryExcel(r));
				} catch (Exception e) {
					errors.add(e.getMessage());
				}
			}
			if (errors.size() > 0) {
				LOG.warn("Ignoring rows with invalid format: {}", String.join(", ", errors));
			}
			return l.stream();
		}
	}
}
