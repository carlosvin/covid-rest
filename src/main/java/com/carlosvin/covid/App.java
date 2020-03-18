package com.carlosvin.covid;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class App {

	public static void main(String[] args) {
		String port = System.getenv("PORT");
		System.setProperty("server.port", port == null || port.length() < 2 ? "8080" : port);
		SpringApplication.run(App.class, args);
	}

}
