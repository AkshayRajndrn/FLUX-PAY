package com.swift.relay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class SwiftMessageRelayServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SwiftMessageRelayServiceApplication.class, args);
	}

}
