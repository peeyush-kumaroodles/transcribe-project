package com.transcribe;

import java.util.Date;
import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@ComponentScan("com.transcribe")
@EnableSwagger2
@EnableMongoAuditing
public class TranscribeProjectApplication {
	

	public static void main(String[] args) {
		SpringApplication.run(TranscribeProjectApplication.class, args);
	}
}
