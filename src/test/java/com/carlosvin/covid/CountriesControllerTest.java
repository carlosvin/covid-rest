package com.carlosvin.covid;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = { "base.url=data-" })
class CountriesControllerTest {
	
	@MockBean
    private Clock clockMock;

	@Autowired
	private MockMvc mockMvc;
	
	@BeforeEach
	void setUp () {
		Clock clock = Clock.fixed(Instant.parse("2020-03-14T10:10:30Z"), ZoneId.of("UTC"));
		Mockito.when(clockMock.getZone()).thenReturn(clock.getZone());
	    Mockito.when(clockMock.instant()).thenReturn(clock.instant());
	    Mockito.when(clockMock.millis()).thenReturn(clock.millis());
	}
	
	@Test
	void getCountries() throws Exception {
		this.mockMvc
			.perform(get("/countries"))
			.andDo(print())
			.andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$", hasSize(122)))
            .andExpect(jsonPath("$[0].confirmedCases", greaterThan(0)))
            .andExpect(jsonPath("$[0].deathsNumber",comparesEqualTo(0)))
            .andExpect(jsonPath("$[0].countryCode",comparesEqualTo("PS")))
            .andExpect(jsonPath("$[0].countryName",comparesEqualTo("Palestine")))
        	.andExpect(jsonPath("$[100].confirmedCases", greaterThan(0)));
	}

	@Test
	void getACountry() throws Exception {
		this.mockMvc
			.perform(get("/countries/ES"))
			.andDo(print())
			.andExpect(status().isOk())
            .andExpect(jsonPath("$.confirmedCases", comparesEqualTo(4231)))
            .andExpect(jsonPath("$.deathsNumber",comparesEqualTo(121)))
            .andExpect(jsonPath("$.countryCode",comparesEqualTo("ES")))
            .andExpect(jsonPath("$.countryName",comparesEqualTo("Spain")));
	}
	
	@Test
	void getDates() throws Exception {
		this.mockMvc
			.perform(get("/countries/ES/dates"))
			.andDo(print())
			.andExpect(status().isOk())
            .andExpect(jsonPath("$.*", hasSize(75)))
            .andExpect(jsonPath("$.18334.confirmedCases", comparesEqualTo(1227)))
            .andExpect(jsonPath("$.18334.deathsNumber",comparesEqualTo(37)))
            .andExpect(jsonPath("$.18334.date", comparesEqualTo("2020-03-13T00:00:00Z")))
            .andExpect(jsonPath("$.18334.epochDays", comparesEqualTo(18334)))
            .andExpect(jsonPath("$.18335").doesNotExist());
	}
	
	@Test
	void getDate() throws Exception {
		this.mockMvc
			.perform(get("/countries/ES/dates/18334"))
			.andDo(print())
			.andExpect(status().isOk())
            .andExpect(jsonPath("$.confirmedCases", comparesEqualTo(1227)))
            .andExpect(jsonPath("$.deathsNumber",comparesEqualTo(37)))
            .andExpect(jsonPath("$.date", comparesEqualTo("2020-03-13T00:00:00Z")))
            .andExpect(jsonPath("$.epochDays", comparesEqualTo(18334)));
	}
	
}
