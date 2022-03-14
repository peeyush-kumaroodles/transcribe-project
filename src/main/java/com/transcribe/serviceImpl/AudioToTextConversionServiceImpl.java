package com.transcribe.serviceImpl;
import java.io.InputStream;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.transcribe.AmazonTranscribe;
import com.amazonaws.services.transcribe.model.Media;
import com.amazonaws.services.transcribe.model.StartTranscriptionJobRequest;
import com.transcribe.dto.AmazonS3Properties;
import com.transcribe.dto.RandomUtils;
import com.transcribe.service.AudioToTextConversionService;
import com.transcribe.service.StorageService;
import lombok.Data;
import lombok.var;

@Service
@Data
@EnableConfigurationProperties(AmazonS3Properties.class)
public class AudioToTextConversionServiceImpl implements AudioToTextConversionService {

	private final StorageService storageService;
	private final AmazonTranscribe amazonTranscribe;
	private final AmazonS3Properties amazonS3Properties;

	public InputStream convertAudioToText(final MultipartFile file, final String languageCode) {
		storageService.saveData(file);
		// Prepare a Media object for the above saved file
		final var media = new Media();
		media.setMediaFileUri(constructS3Link(file));
		// Preparing a TranscriptionJobRequest
		final String jobName = RandomUtils.randomJob(8);
		StartTranscriptionJobRequest startTranscriptionJobRequest = new StartTranscriptionJobRequest();
		startTranscriptionJobRequest.setLanguageCode(languageCode);
		startTranscriptionJobRequest.setOutputBucketName(amazonS3Properties.getAmazonS3().getOutputBucketName());
		startTranscriptionJobRequest.setTranscriptionJobName(jobName);
		startTranscriptionJobRequest.setMedia(media);
		amazonTranscribe.startTranscriptionJob(startTranscriptionJobRequest);
		Boolean resultRetreived = false;
		S3Object result = null;
		while (!resultRetreived) {
			result = storageService.retrieveTranscribedJson(jobName);
			if (result != null)
				resultRetreived = true;
		}
		return result.getObjectContent();
	}
	public String constructS3Link(final MultipartFile file) {
		return "s3://" + amazonS3Properties.getAmazonS3().getInputBucketName() + "/" + file.getOriginalFilename();
	}
}