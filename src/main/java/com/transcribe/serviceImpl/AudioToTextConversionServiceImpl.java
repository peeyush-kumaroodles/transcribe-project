package com.transcribe.serviceImpl;

import java.io.InputStream;
import java.util.Random;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.transcribe.AmazonTranscribe;
import com.amazonaws.services.transcribe.model.Media;
import com.amazonaws.services.transcribe.model.StartTranscriptionJobRequest;
import com.transcribe.dto.AmazonS3Properties;
import com.transcribe.service.AudioToTextConversionService;
import lombok.Data;
import lombok.var;

@Service
@Data
@EnableConfigurationProperties(AmazonS3Properties.class)
public class AudioToTextConversionServiceImpl implements AudioToTextConversionService {

	private final AmazonTranscribe AMAZONTRANSCRIBE;
	private final AmazonS3Properties AMAZONS3PROPERTIES;
	private final AmazonS3 AMAZONS3;

	public InputStream convertAudioToText(final String languageCode) {
		final var media = new Media();
		media.setMediaFileUri(getUrl());
		//final String jobName = randomJob(8);
		String jobName = System.currentTimeMillis() + "_" + "video.mp4";
		StartTranscriptionJobRequest startTranscriptionJobRequest = new StartTranscriptionJobRequest();
		startTranscriptionJobRequest.setLanguageCode(languageCode);
		startTranscriptionJobRequest.setOutputBucketName(AMAZONS3PROPERTIES.getOutputBucketName());
		startTranscriptionJobRequest.setTranscriptionJobName(jobName);
		startTranscriptionJobRequest.setMedia(media);
		AMAZONTRANSCRIBE.startTranscriptionJob(startTranscriptionJobRequest);
		Boolean resultRetreived = false;
		S3Object result = null;
		while (!resultRetreived) {
			result = retrieveTranscribedJson(jobName);
			if (result != null)
				resultRetreived = true;
		}
		return result.getObjectContent();
	}

	@Override
	public String getUrl() {
		return "https://input-file-data.s3.ap-south-1.amazonaws.com/Emma-Watson-English-Motivational-Status-Video.mp4";
	}
	
	@Override
	public S3Object retrieveTranscribedJson(final String jobName) {
		try {
			return AMAZONS3.getObject(AMAZONS3PROPERTIES.getOutputBucketName(), jobName + ".json");
		} catch (AmazonS3Exception e) {
			return null;
		}
	}

	
}