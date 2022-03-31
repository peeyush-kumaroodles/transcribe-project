package com.transcribe.dto;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
@Data
@Document(value = "video_links")
public class VideoLinks {
	@Id
	private String id;
	private long userId;
	private String userType;
	private String messageId;
	private String roomId;
	private String  roomType;
	private LocalDateTime uploadingDateTime;
	private String link;
	private boolean isAudioTranscribe = false;
	private String transcribedFileLink;
}
