package com.carlosvin.covid;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = { "base.url=data-" })
class DatesControllerTest {
	
	@MockBean
    private Clock clockMock;

	@Autowired
	private MockMvc mockMvc;
	
	@BeforeEach
	void setUp () {
		Clock clock = Clock.fixed(Instant.parse("2020-03-17T10:10:30Z"), ZoneId.of("UTC"));
		Mockito.when(clockMock.getZone()).thenReturn(clock.getZone());
	    Mockito.when(clockMock.instant()).thenReturn(clock.instant());
	    Mockito.when(clockMock.millis()).thenReturn(clock.millis());
	}
	
	@Test
	void getDates() throws Exception {
		this.mockMvc
			.perform(get("/dates"))
			.andDo(print())
			.andExpect(status().isOk())
            .andExpect(jsonPath("$.*", hasSize(78)))
			.andExpect(jsonPath("$.18336.confirmedCases", comparesEqualTo(16051)))
            .andExpect(jsonPath("$.18336.deathsNumber",comparesEqualTo(746)))
            .andExpect(jsonPath("$.18336.date", comparesEqualTo("2020-03-15T00:00:00Z")))
            .andExpect(jsonPath("$.18336.epochDays", comparesEqualTo(18336)));
	}
	
}
