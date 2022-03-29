package com.transcribe.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpHeaders;

import com.transcribe.serviceImpl.AudioToTextConversionServiceImpl;
@RestController
public class TranscribeController {
	@Autowired
	private AudioToTextConversionServiceImpl audioToTextConversionServiceImpl;

	@PostMapping(value = "/audio/conversion/url/{url}")
	public ResponseEntity<?> transcribeController(@PathVariable(name = "url") String url) {
		String result = audioToTextConversionServiceImpl.convertAudioToText(url);
	return ResponseEntity.status(HttpStatus.OK)
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=result.json").body(result);
	}
	@GetMapping(value = "/data")
	public ResponseEntity<?> getLink(){
	boolean result=  audioToTextConversionServiceImpl.getTranscribe();
	if(result==true) {
	return new ResponseEntity<String>(HttpStatus.OK);
	}else
		return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
	}
}
