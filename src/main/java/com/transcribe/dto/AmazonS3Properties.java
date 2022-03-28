package com.transcribe.dto;



import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;
@Data
@ConfigurationProperties(prefix = "com.aws")
public class AmazonS3Properties{
	private String inputBucketName;
	private String outputBucketName;
}
