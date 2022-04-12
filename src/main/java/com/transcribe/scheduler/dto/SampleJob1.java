package com.transcribe.scheduler.dto;


import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;


public class SampleJob1 implements Job {
	
	//@Autowired AudioToTextConversionServiceImpl audioToTextConversionServiceImpl;
   
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
    	//  audioToTextConversionServiceImpl.getTranscribe();
    	System.out.println("job1 is executing :testing");
    }

}
