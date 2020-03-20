package com.carlosvin.covid;

import static org.hamcrest.Matchers.comparesEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
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
class DatesControllerTest {
	
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
	void getDates() throws Exception {
		this.mockMvc
			.perform(get("/dates"))
			.andDo(print())
			.andExpect(status().isOk())
            .andExpect(jsonPath("$.*", hasSize(78)))
			.andExpect(jsonPath("$.2020-03-15.confirmedCases", comparesEqualTo(16051)))
            .andExpect(jsonPath("$.2020-03-15.deathsNumber",comparesEqualTo(746)))
            .andExpect(jsonPath("$.2020-03-15.dateTime", comparesEqualTo("2020-03-15T00:00:00Z")))
            .andExpect(jsonPath("$.2020-03-15.date", comparesEqualTo("2020-03-15")))
            .andExpect(jsonPath("$.2020-03-15.path", comparesEqualTo("/dates/2020-03-15")))
			.andDo(document("dates/list", preprocessResponse(prettyPrint(), new CropPreprocessor())));
	}
	
	@Test
	void getDate() throws Exception {
		this.mockMvc
			.perform(get("/dates/2020-03-15"))
			.andDo(document("dates/date", preprocessResponse(prettyPrint())))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.confirmedCases", comparesEqualTo(16051)))
            .andExpect(jsonPath("$.deathsNumber",comparesEqualTo(746)))
            .andExpect(jsonPath("$.dateTime", comparesEqualTo("2020-03-15T00:00:00Z")))
            .andExpect(jsonPath("$.date", comparesEqualTo("2020-03-15")));
	}
	
	@Test
	void getCoutryStatsInADate() throws Exception {
		this.mockMvc
			.perform(get("/dates/2020-03-15/countries"))
			.andDo(print())
			.andExpect(status().isOk())
            .andExpect(jsonPath("$.*", hasSize(128)))
			.andExpect(jsonPath("$.ES.confirmedCases", comparesEqualTo(2000)))
            .andExpect(jsonPath("$.ES.deathsNumber",comparesEqualTo(152)))
            .andExpect(jsonPath("$.ES.countryName", comparesEqualTo("Spain")))
			.andDo(document("dates/date-countries", preprocessResponse(prettyPrint(), new CropPreprocessor())));
	}
	
	@Test
	void getCountry() throws Exception {
		this.mockMvc
			.perform(get("/dates/2020-03-13/countries/eS"))
			.andDo(document("dates/date-country", preprocessResponse(prettyPrint())))
			.andDo(print())
			.andExpect(status().isOk())
            .andExpect(jsonPath("$.confirmedCases", comparesEqualTo(1227)))
            .andExpect(jsonPath("$.deathsNumber",comparesEqualTo(37)))
            .andExpect(jsonPath("$.countryName", comparesEqualTo("Spain")));
	}
	
	
}
