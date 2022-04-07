package com.transcribe.scheduler.dto;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.transcribe.serviceImpl.AudioToTextConversionServiceImpl;

@Slf4j
@Component
public class SampleJob implements Job {

//	@Autowired
//	AudioToTextConversionServiceImpl audioToTextConversionServiceImpl;
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info("Job triggered - Sample Job");
        JobDataMap map = context.getMergedJobDataMap();
        printData(map);
        log.info("Job completed");
    }
    private void printData(JobDataMap map) {
        log.info(">>>>>>>>>>>>>>>>>>> START: ");
        map.entrySet().forEach(entry -> {
            log.info(entry.getKey() + " " + entry.getValue());
        });
        log.info(">>>>>>>>>>>>>>>>>>> END: ");
    }
}
