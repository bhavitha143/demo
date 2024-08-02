package com.bh.realtrack.controller;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan(basePackages = ("com.bh.**"))
@EnableAutoConfiguration(exclude = { SecurityAutoConfiguration.class })
@EnableConfigurationProperties
@EnableScheduling
public class BillingApplication {

	public static void main(final String[] args) {
		SpringApplication.run(BillingApplication.class, args);
	}

}
