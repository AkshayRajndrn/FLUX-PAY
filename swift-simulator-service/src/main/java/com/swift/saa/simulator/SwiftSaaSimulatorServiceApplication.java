package com.swift.saa.simulator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class SwiftSaaSimulatorServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SwiftSaaSimulatorServiceApplication.class, args);
	}

}
