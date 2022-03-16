package com.transcribe.configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.transcribe.dto.AwsCredentialProperties;
import lombok.Data;
@Configuration
@Data
@EnableConfigurationProperties(value = AwsCredentialProperties.class)
public class AwsStorageConfiguration {
	@Autowired
	private final AwsCredentialProperties AWSCREDENTIALPROPERTIES;
	@Bean
	public AmazonS3 amazonS3() {
		AWSCredentials awsCredentials = new BasicAWSCredentials(AWSCREDENTIALPROPERTIES.getAccessKey(),
				AWSCREDENTIALPROPERTIES.getSecretAccessKey());
		return AmazonS3ClientBuilder.standard().withRegion(Regions.AP_SOUTH_1)
				.withCredentials(new AWSStaticCredentialsProvider(awsCredentials)).build();
	}
}