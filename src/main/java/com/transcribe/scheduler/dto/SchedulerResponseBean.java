package com.transcribe.scheduler.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonInclude;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SchedulerResponseBean {
	private boolean success;
	private String jobId;
	private String message;
	private String group;
	private Object result;
	private HttpStatus resultCode;

	public SchedulerResponseBean(boolean success, String message) {
		this.success = success;
		this.message = message;
	}

	public SchedulerResponseBean(boolean success, String jobId, String jobGroup, String message) {
		this.success = success;
		this.jobId = jobId;
		this.group = jobGroup;
		this.message = message;
	}
}
