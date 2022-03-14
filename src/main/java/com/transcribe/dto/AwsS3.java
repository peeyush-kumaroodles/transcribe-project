package com.transcribe.dto;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "com.aws")
public class AwsS3 {
	
	private String inputBucketName="input-file-data";
	private String outputBucketName="output-file-data";

}
