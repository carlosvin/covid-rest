package com.carlosvin.covid;

import static org.hamcrest.Matchers.hasSize;
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
class AppTest {
	
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
	void getACountry() throws Exception {
		this.mockMvc
			.perform(get("/countries/ES"))
			.andDo(print())
			.andExpect(status().isOk());
            //.andExpect(jsonPath("$.dates").isArray())
            //.andExpect(jsonPath("$.dates", hasSize(75)));
	}
	
	/*@Test
	void postAndGet() throws Exception {
		MvcResult result = this.mockMvc
			.perform(post("/countries/Spain"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(content().string(containsString("name of ")))
			.andReturn();
		String json = result.getResponse().getContentAsString();
		String id = JsonPath.read(json, "$.id");
		
		this.mockMvc
			.perform(get("/tasks/{id}", id))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(content().json(json));
	}*/

}
