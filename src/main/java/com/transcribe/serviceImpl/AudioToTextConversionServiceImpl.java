package com.transcribe.serviceImpl;
import java.io.File;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;
import com.amazonaws.services.transcribe.AmazonTranscribe;
import com.amazonaws.services.transcribe.AmazonTranscribeClient;
import com.amazonaws.services.transcribe.model.GetTranscriptionJobRequest;
import com.amazonaws.services.transcribe.model.Media;
import com.amazonaws.services.transcribe.model.Settings;
import com.amazonaws.services.transcribe.model.StartTranscriptionJobRequest;
import com.amazonaws.services.transcribe.model.TranscriptionJob;
import com.amazonaws.services.transcribe.model.TranscriptionJobStatus;
import com.google.gson.Gson;
import com.transcribe.dao.TranscribeRepository;
import com.transcribe.dto.AmazonS3Properties;
import com.transcribe.dto.AmazonTranscription;
import com.transcribe.dto.VideoLinks;
import jodd.http.HttpRequest;
import jodd.http.HttpResponse;
import lombok.Data;
@Service
@EnableConfigurationProperties(AmazonS3Properties.class)
@Data
public class AudioToTextConversionServiceImpl {
	private final AmazonS3Properties AMAZONS3PROPERTIES;
	private final AmazonS3 AMAZONS3;
	private TranscriptionJob transcriptionJob;
	@Autowired TranscribeRepository transcribeRepository;
	@Autowired
	private TransferManager transferManager;
	@Autowired
	private JsonToTextConversion jsonToTextConversion;
	private String bucketName = "";
	private String endpointUrl = "https://s3.ap-south-1.amazonaws.com";
	private static final Logger LOGGER = LoggerFactory.getLogger(AudioToTextConversionServiceImpl.class);
	
	public boolean getTranscribe() {
		List<VideoLinks> videoLinkObj = transcribeRepository.findByIsAudioTranscribe(false);
		for (VideoLinks links : videoLinkObj) {
			String link = links.getLink();
			String fileName = link.substring(link.lastIndexOf('/') + 1);
			String fileTranscriptionURL = convertAudioToText(fileName);
			if (fileTranscriptionURL != null) {
				links.setAudioTranscribe(true);
				links.setTranscribedFileLink(fileTranscriptionURL);
				transcribeRepository.save(links);
				return true;
			}
		}
		return false;
	}
	public String convertAudioToText(String fileName) {
		
		String jsonToText = null;
		AmazonTranscription transcription = null;
		AmazonTranscribe transcribe = AmazonTranscribeClient.builder().withRegion("ap-south-1").build();
		String jobName = System.currentTimeMillis() + "_" + "video.mp4";
		Media media = new Media();
		media.setMediaFileUri("https://"+bucketName+". s3.ap-south-1.amazonaws.com/"+ bucketName +"/"+fileName
				);
		StartTranscriptionJobRequest startTranscriptionJobRequest = new StartTranscriptionJobRequest();
	//	startTranscriptionJobRequest.withLanguageCode(LanguageCode.EnUS);
		startTranscriptionJobRequest.setIdentifyLanguage(true);
		startTranscriptionJobRequest.setTranscriptionJobName(jobName);
		startTranscriptionJobRequest.setMedia(media);
		startTranscriptionJobRequest.withMediaFormat("mp4");
		Settings settings = new Settings();
		settings.withMaxSpeakerLabels(10).withShowSpeakerLabels(true);
	//	startTranscriptionJobRequest.withMedia(media).withMediaSampleRateHertz(48000);
		startTranscriptionJobRequest.withSettings(settings);
		transcribe.startTranscriptionJob(startTranscriptionJobRequest);
		GetTranscriptionJobRequest jobRequest = new GetTranscriptionJobRequest();
		jobRequest.setTranscriptionJobName(jobName);
		jsonToText = getTranscribedFileUri(fileName, jobRequest, settings, transcribe, jobRequest, transcription);
		return jsonToText;
	}
	public String getTranscribedFileUri(String fileName, GetTranscriptionJobRequest jobRequest, Settings settings,
			AmazonTranscribe transcribe, GetTranscriptionJobRequest request, AmazonTranscription transcription) {
		String uploadTranscribeJSONFile = null;
		while (true) {
			transcriptionJob = transcribe.getTranscriptionJob(jobRequest).getTranscriptionJob();
			if (transcriptionJob.getTranscriptionJobStatus().equals(TranscriptionJobStatus.COMPLETED.name())) {
				transcriptionJob.withSettings(settings);
				transcription =downloadFromLink(transcriptionJob.getTranscript().getTranscriptFileUri());
				break;
			} else if (transcriptionJob.getTranscriptionJobStatus().equals(TranscriptionJobStatus.FAILED.name())) {
				break;
			}
		}
		uploadTranscribeJSONFile = uploadTranscribeJSONToTextFile(transcription);
		return uploadTranscribeJSONFile;
	}

	private AmazonTranscription downloadFromLink(String uri) {
		HttpResponse response = HttpRequest.get(uri).send();
		String result = response.charset("UTF-8").bodyText();
		Gson gson = new Gson();
		return gson.fromJson(result, AmazonTranscription.class);
	
	}
	
	public String uploadTranscribeJSONToTextFile(AmazonTranscription transcription) {
		try {
			File transcribedTextFile = jsonToTextConversion.JsonToReadableTextFile(transcription);
			if (transcribedTextFile != null && transcribedTextFile.isFile()) {
				String textUniqueFileName = System.currentTimeMillis() + "readableTranscription.txt";
				final PutObjectRequest requestObj = new PutObjectRequest(AMAZONS3PROPERTIES.getOutputBucketName(),
						textUniqueFileName, transcribedTextFile);
				Upload upload = transferManager.upload(requestObj);
				upload.waitForCompletion();
				transcribedTextFile.delete();
				return endpointUrl + "/" + AMAZONS3PROPERTIES.getOutputBucketName() + "/" + textUniqueFileName;
			}
			return null;
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return null;
		}
	}
}