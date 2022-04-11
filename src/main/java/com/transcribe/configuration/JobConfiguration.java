package com.transcribe.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.io.IOException;
import java.util.Properties;

@Slf4j
@Configuration
public class JobConfiguration {
    @Autowired
    CreatingBeanJobFactory jobFactory;

    @Bean
    public SchedulerFactoryBean schedulerFactory(ApplicationContext applicationContext) {
        try {
            SchedulerFactoryBean factoryBean = new SchedulerFactoryBean();
            factoryBean.setQuartzProperties(quartzProperties());
            jobFactory.setApplicationContext(applicationContext);
            factoryBean.setJobFactory(jobFactory);
            factoryBean.setOverwriteExistingJobs(true);
            factoryBean.setSchedulerName("code-job-scheduler");
            log.info(" Quartz Scheduler initialized");
            return factoryBean;
        } catch (Exception e) {
            log.error(
                    "Scheduler can not be initialized, the error is "
                            + e.getMessage());
            return null;
        }
    }
    @Bean
    public Properties quartzProperties() throws IOException {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource("/quartz.properties"));
        propertiesFactoryBean.afterPropertiesSet();
        return propertiesFactoryBean.getObject();
    }
}