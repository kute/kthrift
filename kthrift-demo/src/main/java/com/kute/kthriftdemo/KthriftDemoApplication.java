package com.kute.kthriftdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@ImportResource(locations = {"spring.xml"})
@SpringBootApplication
public class KthriftDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(KthriftDemoApplication.class, args);
	}
}
