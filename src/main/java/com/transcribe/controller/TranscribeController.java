package com.transcribe.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpHeaders;
import com.transcribe.service.AudioToTextConversionService;
import lombok.var;

@RestController
public class TranscribeController {

	@Autowired
	private AudioToTextConversionService audioToTextConversionService;

	@PostMapping(value = "/audio/conversion/language/{languageCode}")
	public ResponseEntity<InputStreamResource> transcribeController(
			@RequestPart(name = "file", required = true) final MultipartFile file,
			@PathVariable(name = "languageCode") final String languageCode) {
		final var result = new InputStreamResource(audioToTextConversionService.convertAudioToText(file, languageCode));
		return ResponseEntity.status(HttpStatus.OK)
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=result.json").body(result);
	}

}
