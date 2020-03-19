package com.carlosvin.covid.services;

import java.io.IOException;
import java.net.URL;
import java.util.stream.Stream;

import com.carlosvin.covid.models.DateCountryStats;

public interface DataSource {
	Stream<DateCountryStats> fetchData () throws IOException;

	Stream<DateCountryStats> fetchData(int minusDays) throws IOException;

	URL getUrl(int daysToSubtract);
}
