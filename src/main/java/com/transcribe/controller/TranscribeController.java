package com.transcribe.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpHeaders;
import com.transcribe.serviceImpl.AudioToTextConversionServiceImpl;

@RestController
public class TranscribeController {

	@Autowired
	private AudioToTextConversionServiceImpl audioToTextConversionServiceImpl;

	@PostMapping(value = "/audio/conversion/fileName/{fileName}")
	public ResponseEntity<?> transcribeController(@PathVariable(name = "fileName") String fileName) {
		String result = audioToTextConversionServiceImpl.convertAudioToText(fileName);
		return ResponseEntity.status(HttpStatus.OK)
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=result.json").body(result);
	}

}
