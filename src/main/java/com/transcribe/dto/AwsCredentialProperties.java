package com.transcribe.dto;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@ConfigurationProperties(prefix = "com.aws")
@Data
public class AwsCredentialProperties {
	private String accessKey="AKIA2WBRMZFKFIFXTXKW";
	private String secretAccessKey="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";		
}
