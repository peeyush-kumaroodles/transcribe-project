package com.transcribe.serviceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.transcribe.scheduler.dto.JobDetailRequestBean;
import com.transcribe.scheduler.dto.SchedulerResponseBean;
import com.transcribe.scheduler.dto.TriggerDetailsRequestBean;

import java.util.Objects;
import java.util.Set;

import static java.time.ZoneId.systemDefault;
import static java.util.UUID.randomUUID;
import static org.quartz.CronExpression.isValidExpression;
import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobKey.jobKey;
import static org.quartz.TriggerBuilder.newTrigger;
import static org.springframework.util.StringUtils.isEmpty;
@Slf4j
@Service
@RequiredArgsConstructor
public class SchedulerService {
	private final Scheduler scheduler;
	@Autowired TriggerDetailsRequestBean detailsRequestBean;
	public SchedulerResponseBean createJob(
			String jobGroup, JobDetailRequestBean jobDetailRequestBean) {
		SchedulerResponseBean responseBean = new SchedulerResponseBean();
		jobDetailRequestBean.setGroup(jobGroup);
		JobDetail jobDetail = jobDetailRequestBean.buildJobDetail();
		Set<Trigger> triggersForJob = jobDetailRequestBean.buildTriggers();
		log.info("About to save job with key - {}", jobDetail.getKey());
		try {
			scheduler.scheduleJob(jobDetail, triggersForJob, false);
			log.info("Job with key - {} saved sucessfully", jobDetail.getKey());
			responseBean.setResult(jobDetailRequestBean);
			responseBean.setResultCode(HttpStatus.CREATED);
		} catch (SchedulerException e) {
			log.error(
					"Could not save job with key - {} due to error - {}",
					jobDetail.getKey(),
					e.getLocalizedMessage());
			throw new IllegalArgumentException(e.getLocalizedMessage());
		}
		return responseBean;
	}
	public  SchedulerResponseBean updateJob(
		            String jobGroup, String jobName, JobDetailRequestBean jobDetailRequestBean) {
		        SchedulerResponseBean responseBean = new SchedulerResponseBean();
	        try {
		            JobDetail oldJobDetail = scheduler.getJobDetail(jobKey(jobName, jobGroup));
		            if (oldJobDetail!=null) {
		                JobDataMap jobDataMap = oldJobDetail.getJobDataMap();
		                jobDataMap.put("jobType", jobDetailRequestBean.getJobType());
		                jobDataMap.put("uniqueKey", jobDetailRequestBean.getUniqueKey());
		                jobDataMap.put("data", jobDetailRequestBean.getData());
		                JobBuilder jb = oldJobDetail.getJobBuilder();
		            	Set<Trigger> triggersForJob = jobDetailRequestBean.buildTriggers();
		                JobDetail newJobDetail = jb.usingJobData(jobDataMap).storeDurably().build();
		                scheduler.scheduleJob(newJobDetail, triggersForJob,true);
		                log.info("Updated job with key - {}", newJobDetail.getKey());
		                responseBean.setResult(jobDetailRequestBean);
		                responseBean.setResultCode(HttpStatus.CREATED);
		                log.info("job updated successfully -{}.{} " ,jobGroup,jobName);
		            }else
		            log.warn("Could not find job with key - {}.{} to update", jobGroup, jobName);
		        } catch (SchedulerException e) {
		            String errorMsg =
		                    String.format(
		                            "Could not find job with key - %s.%s to update due to error -  %s",
		                            jobGroup, jobName, e.getLocalizedMessage());
		            log.error(errorMsg);
		            responseBean.setResultCode(HttpStatus.INTERNAL_SERVER_ERROR);
		            responseBean.setResult(errorMsg);
		        }
		        return responseBean;
	}
	public SchedulerResponseBean deleteJob(String jobGroup, String jobName) {
		SchedulerResponseBean responseBean = new SchedulerResponseBean();
		try {
			scheduler.deleteJob(jobKey(jobName, jobGroup));
			String msg = "Deleted job with key - " + jobGroup + "." + jobName;
			responseBean.setResult(msg);
			responseBean.setResultCode(HttpStatus.OK);
			log.info(msg);
		} catch (SchedulerException e) {
			String errorMsg =
					String.format(
							"Could not find job with key - %s.%s to Delete due to error -  %s",
							jobGroup, jobName, e.getLocalizedMessage());
			log.error(errorMsg);
		}
		return responseBean;
	}
	
	public SchedulerResponseBean pauseJob(String jobGroup, String jobName) {
		SchedulerResponseBean responseBean = new SchedulerResponseBean();
		try {
			scheduler.pauseJob(jobKey(jobName, jobGroup));
			String msg = "Paused job with key - " + jobGroup + "." + jobName;
			responseBean.setResult(msg);
			responseBean.setResultCode(HttpStatus.OK);
			log.info("job paused with key - {}.{} ", jobGroup, jobName);
		} catch (SchedulerException e) {
			String errorMsg =
					String.format(
							"Could not find job with key - %s.%s  due to error -  %s",
							jobGroup, jobName, e.getLocalizedMessage());
			log.error(errorMsg);
			responseBean.setResultCode(HttpStatus.INTERNAL_SERVER_ERROR);
			responseBean.setResult(errorMsg);
		}
		return responseBean;
	}
}