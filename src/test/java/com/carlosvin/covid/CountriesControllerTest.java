package com.carlosvin.covid;

import static org.hamcrest.Matchers.comparesEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
//import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
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

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = { "base.url=data-" })
@AutoConfigureRestDocs(uriHost = "covid-rest.appspot.com", uriScheme = "https")
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
				.andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath("$.*", hasSize(145)))
				.andExpect(jsonPath("$.ES.confirmedCases",comparesEqualTo(9191)))
				.andExpect(jsonPath("$.ES.deathsNumber", comparesEqualTo(309)))
				.andExpect(jsonPath("$.ES.countryCode", comparesEqualTo("ES")))
				.andExpect(jsonPath("$.ES.countryName", comparesEqualTo("Spain")))
				.andExpect(jsonPath("$.ES.path", comparesEqualTo("/countries/ES")))
				.andDo(document("countries/list", preprocessResponse(prettyPrint(), new CropPreprocessor())));
	}

	@Test
	void getACountry() throws Exception {
		this.mockMvc.perform(get("/countries/ES")).andDo(document("countries/country")).andDo(print())
				.andExpect(status().isOk()).andExpect(jsonPath("$.confirmedCases", comparesEqualTo(9191)))
				.andExpect(jsonPath("$.deathsNumber", comparesEqualTo(309)))
				.andExpect(jsonPath("$.countryCode", comparesEqualTo("ES")))
				.andExpect(jsonPath("$.countryName", comparesEqualTo("Spain")));
	}

	@Test
	void getDates() throws Exception {
		this.mockMvc.perform(get("/countries/ES/dates")).andExpect(status().isOk())
				.andExpect(jsonPath("$.*", hasSize(78)))
				.andExpect(jsonPath("$.2020-03-13.confirmedCases", comparesEqualTo(1227)))
				.andExpect(jsonPath("$.2020-03-13.deathsNumber", comparesEqualTo(37)))
				.andExpect(jsonPath("$.2020-03-13.dateTime", comparesEqualTo("2020-03-13T00:00:00Z")))
				.andExpect(jsonPath("$.2020-03-13.date", comparesEqualTo("2020-03-13")))
				.andExpect(jsonPath("$.2020-03-13.path", comparesEqualTo("/countries/ES/dates/2020-03-13")))
				.andExpect(jsonPath("$.2020-03-18").doesNotExist()).andDo(print())
				.andDo(document("countries/country-dates", preprocessResponse(prettyPrint(), new CropPreprocessor())));
	}

	@Test
	void getDate() throws Exception {
		this.mockMvc.perform(get("/countries/Es/dates/2020-03-13")).andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath("$.confirmedCases", comparesEqualTo(1227)))
				.andExpect(jsonPath("$.deathsNumber", comparesEqualTo(37)))
				.andExpect(jsonPath("$.dateTime", comparesEqualTo("2020-03-13T00:00:00Z")))
				.andExpect(jsonPath("$.date", comparesEqualTo("2020-03-13")))
				.andDo(document("countries/country-date", preprocessResponse(prettyPrint())));
	}

}
