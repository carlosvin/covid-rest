package com.carlosvin.covid;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EnableScheduling
public class App {

	public static void main(String[] args) {
		String port = System.getenv("PORT");
		System.setProperty("server.port", port == null || port.length() < 2 ? "8080" : port);
		SpringApplication.run(App.class, args);
	}
	
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry
					.addMapping("/**")
					.allowedHeaders("Origin")
					.allowedOrigins("http://localhost", "*")
					.allowedMethods("GET", "HEAD");
			}
		};
	}
	
}
