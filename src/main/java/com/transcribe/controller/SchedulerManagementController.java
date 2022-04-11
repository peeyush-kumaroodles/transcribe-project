package com.transcribe.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.transcribe.scheduler.dto.JobDetailRequestBean;
import com.transcribe.scheduler.dto.SchedulerResponseBean;
import com.transcribe.serviceImpl.SchedulerService;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/v1/scheduler")
public class SchedulerManagementController {

	public static final String JOBS = "/job-group/{jobGroup}/jobs";
	public static final String JOBS_BY_NAME = "/job-group/{jobGroup}/jobs/{jobName}";
    public static final String JOBS_PAUSE = "/job-group/{jobGroup}/jobs/{jobName}/pause";

	@Autowired
	private SchedulerService schedulerService;

	@PostMapping(path = JOBS)
	public ResponseEntity<SchedulerResponseBean> createJob1(@PathVariable String jobGroup,
			@RequestBody JobDetailRequestBean jobDetailRequestBean) {
		return new ResponseEntity<SchedulerResponseBean>(schedulerService.createJob(jobGroup, jobDetailRequestBean),
				CREATED);
	}
	
	@PutMapping(path = JOBS_BY_NAME)
	public ResponseEntity<SchedulerResponseBean> updateJob(@PathVariable String jobGroup, @PathVariable String jobName,
			@RequestBody JobDetailRequestBean jobDetailRequestBean) {
		return new ResponseEntity<>(schedulerService.updateJob(jobGroup, jobName, jobDetailRequestBean), OK);
	}
	
	 @PatchMapping(path = JOBS_PAUSE)
	    public ResponseEntity<SchedulerResponseBean> pauseJob(
	            @PathVariable String jobGroup, @PathVariable String jobName) {
	        return new ResponseEntity<>(schedulerService.pauseJob(jobGroup, jobName), OK);
	    }
}