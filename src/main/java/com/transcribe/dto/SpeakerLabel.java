package com.transcribe.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class SpeakerLabel {
	private int speakers;
	private List<Segment> segments = new ArrayList<Segment>();
}
