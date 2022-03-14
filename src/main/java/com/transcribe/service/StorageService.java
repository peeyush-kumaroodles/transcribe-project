package com.transcribe.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.model.S3Object;

public interface StorageService {
	public HttpStatus saveData(final MultipartFile file);

	public S3Object retrieveTranscribedJson(final String jobName);

}
