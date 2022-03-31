package com.transcribe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TranscribeProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(TranscribeProjectApplication.class, args);
	}
}
