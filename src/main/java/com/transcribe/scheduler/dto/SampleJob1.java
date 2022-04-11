package com.transcribe.scheduler.dto;


import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import com.transcribe.serviceImpl.AudioToTextConversionServiceImpl;

@Slf4j
public class SampleJob1 implements Job {
	
	//@Autowired AudioToTextConversionServiceImpl audioToTextConversionServiceImpl;
   
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
    	//  audioToTextConversionServiceImpl.getTranscribe();
    	System.out.println("job1 is executing :testing");
    }

}
