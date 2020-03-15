package com.carlosvin.covid;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestConfig {
	
	@Bean(name="baseUrl")
	public String getBaseUrl() {
		return "file:///home/carlos/Downloads/COVID-19-geographic-disbtribution-worldwide-";
	}
}
