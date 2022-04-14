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
	 private Object result;
	    private HttpStatus resultCode;
}
