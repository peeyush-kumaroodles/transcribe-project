package com.transcribe.scheduler.dto;

import lombok.Data;
import org.quartz.Trigger;
import java.io.Serializable;
import java.util.TimeZone;
import static java.time.ZoneId.systemDefault;
import static java.util.UUID.randomUUID;
import static org.quartz.CronExpression.isValidExpression;
import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.TriggerBuilder.newTrigger;
import static org.springframework.util.StringUtils.isEmpty;

@Data
public class TriggerDetailsRequestBean implements Serializable {
    private String name;
    private String group;
    private String cron;
    public static TriggerDetailsRequestBean buildTriggerDetails(Trigger trigger) {
        return new TriggerDetailsRequestBean()
                .setName(trigger.getKey().getName())
                .setGroup(trigger.getKey().getGroup())
                .setCron(trigger.getJobDataMap().getString("cron"));
    }
    public TriggerDetailsRequestBean setCron(final String cron) {
        this.cron = cron;
        return this;
    }

    public TriggerDetailsRequestBean setGroup(final String group) {
        this.group = group;
        return this;
    }
    public TriggerDetailsRequestBean setName(final String name) {
        this.name = name;
        return this;
    }
    public Trigger buildTrigger() {
        if (!isEmpty(cron)) {
            if (!isValidExpression(cron))
                throw new IllegalArgumentException(
                        "Provided expression " + cron + " is not a valid cron expression");
            return newTrigger()
                    .withIdentity(buildName(), group)
                    .withSchedule(
                            cronSchedule(cron)
                                    .withMisfireHandlingInstructionFireAndProceed()
                                    .inTimeZone(TimeZone.getTimeZone(systemDefault())))
                    .usingJobData("cron", cron)
                    .build();
        }
        throw new IllegalStateException("unsupported trigger details " + this);
    }

    private String buildName() {
        return isEmpty(name) ? randomUUID().toString() : name;
    }
}