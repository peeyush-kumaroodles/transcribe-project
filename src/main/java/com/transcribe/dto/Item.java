package com.transcribe.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class Item {
	
	private String start_time;
    private String end_time;
    private List<Alternative> alternatives = new ArrayList<Alternative>();
    private String type;

}
