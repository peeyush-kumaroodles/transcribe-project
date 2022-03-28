package com.transcribe.dto;

import java.util.ArrayList;
import java.util.List;


import lombok.Data;

@Data
public class Result {
	private List<Transcript> transcripts = new ArrayList<Transcript>();
	private List<Item> items = new ArrayList<Item>();
	private SpeakerLabel speaker_labels;

}
