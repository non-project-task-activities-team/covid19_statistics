package com.trainingtask.dataprocessor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class DataProcessorApplication {

	public static void main(String[] args) {
		SpringApplication.run(DataProcessorApplication.class, args);
	}

}
