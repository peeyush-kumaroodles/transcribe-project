package com.transcribe.scheduler.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Trigger;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import static org.quartz.JobBuilder.newJob;

@Data
public class JobDetailRequestBean implements Serializable {
	private String name;
	@Email
	@NotEmpty
	private String email;
	@NotEmpty
	private String subject;
	@NotEmpty
	private String body;
	private String group;
	@JsonProperty("triggers")
	private List<TriggerDetailsRequestBean> triggerDetails = new ArrayList<>();
	private String jobType;
	private String uniqueKey;
	private Map<String, Object> data = new LinkedHashMap<>();

	public static JobDetailRequestBean buildJobDetail(JobDetail jobDetail, List<? extends Trigger> triggersOfJob) {
		List<TriggerDetailsRequestBean> triggerDetailsRequestBeanList = triggersOfJob.stream()
				.map(TriggerDetailsRequestBean::buildTriggerDetails).collect(Collectors.toList());
		return new JobDetailRequestBean().setName(jobDetail.getKey().getName()).setGroup(jobDetail.getKey().getGroup())
				.setUniqueKey(jobDetail.getJobDataMap().getString("uniqueKey"))
				.setJobType(jobDetail.getJobDataMap().getString("jobType"))
				.setData((Map<String, Object>) jobDetail.getJobDataMap().get("data"))
				.setTriggerDetails(triggerDetailsRequestBeanList);
	}

	public JobDetailRequestBean setTriggerDetails(final List<TriggerDetailsRequestBean> triggerDetails) {
		this.triggerDetails = triggerDetails;
		return this;
	}

	public JobDetailRequestBean setData(final Map<String, Object> data) {
		this.data = data;
		return this;
	}

	public JobDetailRequestBean setUniqueKey(String uniqueKey) {
		this.uniqueKey = uniqueKey;
		return this;
	}
	public JobDetailRequestBean setEmail(String email) {
		this.email = email;
		return this;
	}

	public JobDetailRequestBean setSubject(String subject) {
		this.subject = subject;
		return this;
	}

	public JobDetailRequestBean setBody(String body) {
		this.body = body;
		return this;
	}

	public JobDetailRequestBean setJobType(String jobType) {
		this.jobType = jobType;
		return this;
	}

	public JobDetailRequestBean setGroup(final String group) {
		this.group = group;
		return this;
	}

	public JobDetailRequestBean setName(final String name) {
		this.name = name;
		return this;
	}

	public JobDetail buildJobDetail() {
		JobDataMap jobDataMap = new JobDataMap(getData());
		jobDataMap.put("jobType", jobType);
		jobDataMap.put("uniqueKey", uniqueKey);
		jobDataMap.put("data", data);
		return newJob(SampleJob1.class).withIdentity(getName(), getGroup()).usingJobData(jobDataMap).build();
	}

	@JsonIgnore
	public Set<Trigger> buildTriggers() {
		return triggerDetails.stream().map(TriggerDetailsRequestBean::buildTrigger)
				.collect(Collectors.toCollection(LinkedHashSet::new));
	}
}