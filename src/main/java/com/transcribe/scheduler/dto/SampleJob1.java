package com.transcribe.scheduler.dto;
import java.nio.charset.StandardCharsets;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.quartz.JobDataMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;
import com.transcribe.serviceImpl.AudioToTextConversionServiceImpl;
@Component
public class SampleJob1 extends QuartzJobBean {
	private static final Logger logger = LoggerFactory.getLogger(SampleJob1.class);

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private MailProperties mailProperties;

	@Autowired AudioToTextConversionServiceImpl audioToTextConversionServiceImpl;

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		System.out.println("job1 is executing");
		//	audioToTextConversionServiceImpl.getAllVidepoLink();
		JobDataMap jobDataMap = context.getMergedJobDataMap();
		String subject = jobDataMap.getString("subject");
		String body = jobDataMap.getString("body");
		String recipientEmail = jobDataMap.getString("email");
		ResponseEntity<Object> links=	audioToTextConversionServiceImpl.getAllVidepoLink();
		System.out.println(links);

		//sendMail(mailProperties.getUsername(), recipientEmail, subject, body);
	}

	private void sendMail(String fromEmail, String toEmail, String subject, String body) {
		try {
			logger.info("Sending Email to {}", toEmail);
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper messageHelper = new MimeMessageHelper(message, StandardCharsets.UTF_8.toString());
			messageHelper.setSubject(subject);
			messageHelper.setText(body, true);
			messageHelper.setFrom(fromEmail);
			messageHelper.setTo(toEmail);
			mailSender.send(message);
		} catch (MessagingException ex) {
			logger.error("Failed to send email to {}", toEmail);
		}
	}
}
