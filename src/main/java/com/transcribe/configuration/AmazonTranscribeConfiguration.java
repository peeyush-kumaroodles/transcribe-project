package com.transcribe.configuration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.transcribe.AmazonTranscribe;
import com.amazonaws.services.transcribe.AmazonTranscribeClientBuilder;
import com.transcribe.dto.AwsCredentialProperties;
import lombok.Data;
@Configuration
@EnableConfigurationProperties(value = AwsCredentialProperties.class)
@Data
public class AmazonTranscribeConfiguration {
	
	private final AwsCredentialProperties awsCredentialProperties;
	@Bean
	public AmazonTranscribe amazonTranscribe() {
		BasicAWSCredentials credentials = new BasicAWSCredentials(awsCredentialProperties.getAccessKey(),
				awsCredentialProperties.getSecretAccessKey());

		return AmazonTranscribeClientBuilder.standard().withRegion(Regions.AP_SOUTH_1)
				.withCredentials(new AWSStaticCredentialsProvider(credentials)).build();
	}
}
