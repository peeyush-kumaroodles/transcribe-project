package com.transcribe.serviceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.transcribe.scheduler.dto.JobDetailRequestBean;
import com.transcribe.scheduler.dto.SchedulerResponseBean;
import java.util.Objects;
import java.util.Set;
import static org.quartz.JobKey.jobKey;
@Slf4j
@Service
@RequiredArgsConstructor
public class SchedulerService {
	private final Scheduler scheduler;

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
	public SchedulerResponseBean updateJob(
			String jobGroup, String jobName, JobDetailRequestBean jobDetailRequestBean) {
		SchedulerResponseBean responseBean = new SchedulerResponseBean();
		try {
			JobDetail oldJobDetail = scheduler.getJobDetail(jobKey(jobName, jobGroup));
			if (Objects.nonNull(oldJobDetail)) {
				JobDataMap jobDataMap = oldJobDetail.getJobDataMap();
				jobDataMap.put("jobType", jobDetailRequestBean.getJobType());
				jobDataMap.put("uniqueKey", jobDetailRequestBean.getUniqueKey());
				jobDataMap.put("data", jobDetailRequestBean.getData());
				JobBuilder jb = oldJobDetail.getJobBuilder();
				JobDetail newJobDetail = jb.usingJobData(jobDataMap).storeDurably().build();
				scheduler.addJob(newJobDetail, true);
				log.info("Updated job with key - {}", newJobDetail.getKey());
				responseBean.setResult(jobDetailRequestBean);
				responseBean.setResultCode(HttpStatus.CREATED);
			}
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
}