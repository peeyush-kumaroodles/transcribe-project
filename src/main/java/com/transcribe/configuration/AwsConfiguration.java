package com.transcribe.configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.comprehend.AmazonComprehend;
import com.amazonaws.services.comprehend.AmazonComprehendClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.transcribe.AmazonTranscribe;
import com.amazonaws.services.transcribe.AmazonTranscribeClientBuilder;
import com.transcribe.dto.AwsCredentialProperties;
import lombok.Data;
@Configuration
@Data
@EnableConfigurationProperties(value = AwsCredentialProperties.class)
public class AwsConfiguration {
	@Autowired
	private final AwsCredentialProperties AWSCREDENTIALPROPERTIES;
	@Bean
	public AmazonS3 amazonS3() {
		AWSCredentials awsCredentials = new BasicAWSCredentials(AWSCREDENTIALPROPERTIES.getAccessKey(),
				AWSCREDENTIALPROPERTIES.getSecretAccessKey());
		return AmazonS3ClientBuilder.standard().withRegion(Regions.AP_SOUTH_1)
				.withCredentials(new AWSStaticCredentialsProvider(awsCredentials)).build();
	}
	@Bean
	public AmazonTranscribe amazonTranscribe() {
		BasicAWSCredentials credentials = new BasicAWSCredentials(AWSCREDENTIALPROPERTIES.getAccessKey(),
				AWSCREDENTIALPROPERTIES.getSecretAccessKey());

		return AmazonTranscribeClientBuilder.standard().withRegion(Regions.AP_SOUTH_1)
				.withCredentials(new AWSStaticCredentialsProvider(credentials)).build();
	}
	@Bean
	public TransferManager transferManager() {
		return TransferManagerBuilder.standard().withS3Client(amazonS3())
				.withDisableParallelDownloads(false).withMinimumUploadPartSize(5120L)
				.withMultipartUploadThreshold(16384L).withMultipartCopyPartSize(5120L)
				//.withMultipartCopyThreshold(102400L).withExecutorFactory(() -> createExecutorService(20))
				.build();
	}

}