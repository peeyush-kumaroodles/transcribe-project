package com.transcribe.serviceImpl;
import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.transcribe.dto.AmazonS3Properties;
import com.transcribe.service.StorageService;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Service
@Data
@EnableConfigurationProperties(value = AmazonS3Properties.class)
@Slf4j
public class StorageServiceImpl implements StorageService {

	private final AmazonS3 amazonS3;
	private final AmazonS3Properties amazonS3Properties;
	public HttpStatus saveData(final MultipartFile file) {
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(file.getSize());
		metadata.setContentType(file.getContentType());
		metadata.setContentDisposition(file.getOriginalFilename());

		try {
			amazonS3.putObject(amazonS3Properties.getAmazonS3().getInputBucketName(), file.getOriginalFilename(),
					file.getInputStream(), metadata);
		} catch (SdkClientException | IOException e) {
			log.error("UNABLE TO STORE {} IN S3: {} ", file.getOriginalFilename(), LocalDateTime.now());
			return HttpStatus.EXPECTATION_FAILED;
		}
		return HttpStatus.OK;
	}
	// Method to retrieve transcribed JSON result from configured output bucket
	public S3Object retrieveTranscribedJson(final String jobName) {
		try {
			return amazonS3.getObject(amazonS3Properties.getAmazonS3().getOutputBucketName(), jobName + ".json");
		} catch (AmazonS3Exception e) {
			return null;
		}
	}
}