package com.transcribe.serviceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.transcribe.scheduler.dto.JobDetailRequestBean;
import com.transcribe.scheduler.dto.SchedulerResponseBean;
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
		JobDetail jobDetail = jobDetailRequestBean.buildJobDetail1();
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
//	public SchedulerResponseBean updateJob(
//			String jobGroup, String jobName, JobDetailRequestBean jobDetailRequestBean) {
//		SchedulerResponseBean responseBean = new SchedulerResponseBean();
//		try {
//			JobDetail oldJobDetail = scheduler.getJobDetail(jobKey(jobName, jobGroup));
//			if (oldJobDetail!=null) {
//				JobDataMap jobDataMap = oldJobDetail.getJobDataMap();
//				jobDataMap.put("jobType", jobDetailRequestBean.getJobType());
//				jobDataMap.put("uniqueKey", jobDetailRequestBean.getUniqueKey());
//				jobDataMap.put("data", jobDetailRequestBean.getData());
//				JobBuilder jb = oldJobDetail.getJobBuilder();
//				JobDetail newJobDetail = jb.usingJobData(jobDataMap).storeDurably().build();
//				//Set<Trigger> triggersForJob = jobDetailRequestBean.buildTriggers();
//			//	scheduler.scheduleJob((Trigger) triggersForJob);
//				//scheduler.addJob(newJobDetail, true);
//				scheduler.addJob(oldJobDetail, true, true);
//				log.info("Updated job with key - {}", newJobDetail.getKey());
//				responseBean.setResult(jobDetailRequestBean);
//				responseBean.setResultCode(HttpStatus.CREATED);
//			}else
//				log.warn("Could not find job with - {}.{} to update", jobGroup, jobName);
//		} catch (SchedulerException e) {
//			String errorMsg =
//					String.format(
//							"Could not find job with key - %s.%s to update due to error -  %s",
//							jobGroup, jobName, e.getLocalizedMessage());
//			log.error(errorMsg);
//		}
//		return responseBean;
//	}

		public SchedulerResponseBean updateJob(
				String jobGroup, String jobName, JobDetailRequestBean jobDetailRequestBean) {
			SchedulerResponseBean responseBean = new SchedulerResponseBean();
			try {
				scheduler.deleteJob(jobKey(jobName, jobGroup));
			} catch (SchedulerException e) {
				e.printStackTrace();
			}
			responseBean= createJob(jobGroup, jobDetailRequestBean);
			String msg = "job updated  with key - " + jobGroup + "." + jobName;
			log.info(msg);
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