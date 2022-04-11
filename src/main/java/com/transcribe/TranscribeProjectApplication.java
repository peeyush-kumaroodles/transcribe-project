package com.transcribe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
//@EnableScheduling
@EnableMongoAuditing
public class TranscribeProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(TranscribeProjectApplication.class, args);
	}
}
