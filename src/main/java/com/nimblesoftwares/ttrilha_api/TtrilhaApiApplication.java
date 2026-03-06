package com.nimblesoftwares.ttrilha_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class TtrilhaApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(TtrilhaApiApplication.class, args);
	}

}
