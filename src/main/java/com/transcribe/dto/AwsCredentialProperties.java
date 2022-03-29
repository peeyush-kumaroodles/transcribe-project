package com.transcribe.dto;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@ConfigurationProperties(prefix = "com.aws")
@Data
public class AwsCredentialProperties {
	private String accessKey;
	private String secretAccessKey;		
}
