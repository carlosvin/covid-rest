package com.carlosvin.covid;

import static org.hamcrest.Matchers.comparesEqualTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = { "base.url=data-" })
@AutoConfigureRestDocs
class CountriesControllerTest {

	@TestConfiguration
	public static class TestConfig {

		@Bean
		@Primary
		public Clock mockClock() {
			return Clock.fixed(Instant.parse("2020-03-17T10:10:30Z"), ZoneId.of("UTC"));
		}

	}

	@Autowired
	private MockMvc mockMvc;
	
	

	@Test
	void getCountries() throws Exception {
		this.mockMvc.perform(get("/countries"))
			.andDo(document("countries/list", preprocessResponse(prettyPrint())))
			.andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath("$").isArray()).andExpect(jsonPath("$", hasSize(145)))
				.andExpect(jsonPath("$[0].confirmedCases", greaterThan(0)))
				.andExpect(jsonPath("$[0].deathsNumber", comparesEqualTo(0)))
				.andExpect(jsonPath("$[0].countryCode", comparesEqualTo("PS")))
				.andExpect(jsonPath("$[0].countryName", comparesEqualTo("Palestine")))
				.andExpect(jsonPath("$[100].confirmedCases", greaterThan(0)));
	}

	@Test
	void getACountry() throws Exception {
		this.mockMvc.perform(get("/countries/ES")).andDo(document("countries/country")).andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath("$.confirmedCases", comparesEqualTo(9191)))
				.andExpect(jsonPath("$.deathsNumber", comparesEqualTo(309)))
				.andExpect(jsonPath("$.countryCode", comparesEqualTo("ES")))
				.andExpect(jsonPath("$.countryName", comparesEqualTo("Spain")));
	}

	@Test
	void getDates() throws Exception {
		this.mockMvc.perform(get("/countries/ES/dates")).andDo(print()).andDo(document("countries/country-dates"))
				.andExpect(status().isOk()).andExpect(jsonPath("$.*", hasSize(78)))
				.andExpect(jsonPath("$.18334.confirmedCases", comparesEqualTo(1227)))
				.andExpect(jsonPath("$.18334.deathsNumber", comparesEqualTo(37)))
				.andExpect(jsonPath("$.18334.date", comparesEqualTo("2020-03-13T00:00:00Z")))
				.andExpect(jsonPath("$.18334.epochDays", comparesEqualTo(18334)))
				.andExpect(jsonPath("$.18338").doesNotExist());
	}

	@Test
	void getDate() throws Exception {
		this.mockMvc.perform(get("/countries/Es/dates/18334")).andDo(document("countries/country-date")).andDo(print())
				.andExpect(status().isOk()).andExpect(jsonPath("$.confirmedCases", comparesEqualTo(1227)))
				.andExpect(jsonPath("$.deathsNumber", comparesEqualTo(37)))
				.andExpect(jsonPath("$.date", comparesEqualTo("2020-03-13T00:00:00Z")))
				.andExpect(jsonPath("$.epochDays", comparesEqualTo(18334)));
	}

}
