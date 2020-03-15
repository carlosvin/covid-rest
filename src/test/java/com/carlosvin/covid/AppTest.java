package com.carlosvin.covid;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = { "url.base=classpath:data-" })
class AppTest {

	@Autowired
	private MockMvc mockMvc;

	/*
	 * public String getBaseUrl() { URL url = getClass() .getClassLoader()
	 * .getResource("COVID-19-geographic-disbtribution-worldwide-2020-03-15.xls");
	 * String urlS = url.toString(); return urlS.substring(0, urlS.length() - 4); }
	 */
	
	@Test
	void empty() throws Exception {
		this.mockMvc
			.perform(get("/countries/ES"))
			.andDo(print())
			.andExpect(status().isOk());
			//.andExpect(content().string("[]"));
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
