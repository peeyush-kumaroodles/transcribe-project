package com.transcribe.dto;

import lombok.Data;

@Data
public class AmazonTranscription {
	
	private String jobName;
    private String accountId;
    private Result results;
    private String status;

}
