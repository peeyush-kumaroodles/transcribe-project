package com.transcribe.service;

import java.io.InputStream;

import org.springframework.web.multipart.MultipartFile;

public interface AudioToTextConversionService {
	public InputStream convertAudioToText(final MultipartFile file, final String languageCode);

	public String constructS3Link(final MultipartFile file);
}
