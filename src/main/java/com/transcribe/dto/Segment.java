package com.transcribe.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class Segment {
	private String start_time;
    private String end_time;
    private String speaker_label;
    private List<SegmentItem> items = new ArrayList<SegmentItem>();


}
