package com.transcribe.dto;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
@Data
//@Document(value = "video_links")
public class VideoLinks {
	@Id
	String link;
	String messageId;
	String roomAccessKey;
	String roomId;
	String roomType;
	private boolean isAudioTranscribe = false;
	private String transcribedFileLink;
}
