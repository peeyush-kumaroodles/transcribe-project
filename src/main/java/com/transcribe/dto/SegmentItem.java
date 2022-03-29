package com.transcribe.dto;

import lombok.Data;

@Data
public class SegmentItem {
	private String start_time;
    private String end_time;
    private String speaker_label;   
}
