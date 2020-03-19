package com.carlosvin.covid.services.excel;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.time.Clock;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import com.carlosvin.covid.models.DateCountryStats;
import com.carlosvin.covid.services.DataSource;
import com.carlosvin.covid.services.DownloadManager;

@Service
@PropertySource("classpath:application.properties")
public class DataSourceExcel implements DataSource {
	private static String [] EXT = {".xls", ".xlsx"};
	private static final Logger LOG = LoggerFactory.getLogger(DataSourceExcel.class);
	private static final DateTimeFormatter DF = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	
	private final Path tmpPath;
	private final String baseUrl;
	private final Clock clock;
	private final DownloadManager dm;
	
	@Autowired
	public DataSourceExcel(@Value("${base.url.excel}") String baseUrl, Clock clock, DownloadManager dm) throws IOException {
		this.baseUrl = baseUrl;
		this.clock = clock;
		this.dm = dm;
		File f = File.createTempFile("downloaded", ".dl");
		f.deleteOnExit();
		this.tmpPath = f.toPath(); 
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
				return url.openStream();
			} catch (IOException e) {
				LOG.info("URL not found {}: {}", e.getMessage(), e.getCause());
			}
		}
		throw new IOException("Cannot open stream for any of configured URLs");
	}
	
	@Override
	public Stream<DateCountryStats> fetchData(int daysToSubtract) throws IOException {
		long data = dm.download(openStream(daysToSubtract), tmpPath);
		LOG.info("Saved {} bytes to {}", data, tmpPath);
		try (Workbook wb = WorkbookFactory.create(tmpPath.toFile())) {
			return StreamSupport
					.stream(wb.getSheetAt(0).spliterator(), false)
					.skip(1)
					.map(r -> new CovidDataEntryExcel(r));
			
		}
	}

}
