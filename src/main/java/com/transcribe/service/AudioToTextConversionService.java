package com.transcribe.service;

import java.io.InputStream;

import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.model.S3Object;

public interface AudioToTextConversionService {
	public InputStream convertAudioToText( final String languageCode);

	public String getUrl();
	
	public S3Object retrieveTranscribedJson(final String jobName);
}
