package com.carlosvin.covid.services;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import javax.annotation.Resource;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import com.carlosvin.covid.models.CovidDataEntry;

@Service
@PropertySource("classpath:application.properties")
public class DataSourceExcel implements DataSource {
	private static final DateTimeFormatter DF = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	private final String baseUrl;
	
	public DataSourceExcel(@Value("${base.url}") String baseUrl) {
		this.baseUrl = baseUrl;
	}

	
	private URL getUrl() throws MalformedURLException {
		return new URL(String.format(baseUrl + DF.format(LocalDate.now()) + ".xls"));
	}

	@Override
	public Stream<CovidDataEntry> fetchData() throws IOException {
		try (Workbook wb = WorkbookFactory.create(getUrl().openStream())) {
			Sheet sheet = wb.getSheetAt(0);
			sheet.removeRow(sheet.getRow(0));
			return StreamSupport.stream(sheet.spliterator(), false).map(r -> new CovidDataEntryExcel(r));
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		} catch (EncryptedDocumentException | InvalidFormatException e) {
			throw new IOException(e);
		}
	}

}
