package com.carlosvin.covid.services;

import java.io.IOException;
import java.util.stream.Stream;

import com.carlosvin.covid.models.CovidDataEntry;

public interface DataSource {
	Stream<CovidDataEntry> fetchData () throws IOException;
}
