package com.transcribe.serviceImpl;
import java.io.File;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;
import com.amazonaws.services.transcribe.AmazonTranscribe;
import com.amazonaws.services.transcribe.AmazonTranscribeClient;
import com.amazonaws.services.transcribe.model.GetTranscriptionJobRequest;
import com.amazonaws.services.transcribe.model.LanguageCode;
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
import lombok.extern.slf4j.Slf4j;
@Service
@Slf4j
@EnableConfigurationProperties(AmazonS3Properties.class)
@Data
public class AudioToTextConversionServiceImpl {
	//@Autowired VideoLinks links;
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
	//@Scheduled(fixedRate = 10000 ,initialDelay = 10000)
	//	@Scheduled(cron = "* 42 12 * * *")
	//	public void fetchData() {
	//		List<VideoLinks> list=transcribeRepository.findAll();
	//		for (VideoLinks videoLinks : list) {
	//			System.out.println(list);
	//		}
	//	}
	@Autowired
	private RestTemplate restTemplate;
	
	
	public ResponseEntity<VideoLinks> exchangeMethodOfRestTemplate(){
		VideoLinks videoLink=new VideoLinks();
		videoLink.setLink("https://message-file-upload-1.s3.ap-south-1.amazonaws.com/tWxaqygh_Fz_ceat_LTG45Syod2.mp4");
		videoLink.setMessageId("1143aa809e902754222542");
		videoLink.setRoomAccessKey("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI2ODQwOGYxNjQwNjg4NzczNjU1NjFiM2Q1In0.1BgwrKTxDUxg6fMKfJ3QGGP2eTHLuQqHVB32z2lu4g4");
		videoLink.setRoomId("68408f164068877365561b3d5");
		videoLink.setRoomType("CHATROOM");
		String baseURL = "http://103.46.238.132:8080/v1/user/room/videoLink";
		String auth = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiLCJST0xFX0FETUlOIiwiUk9MRV9TVVBFUkFETUlOIiwiUk9MRV9TVVBFUl9BRE1JTiJdLCJpcCI6IjEwMy40Ni4yMzkuMTM5IiwidXNlcklkIjoxMDAwLCJlbWFpbCI6InBhbmthai5yYWpAb29kbGVzLmlvIiwidXNlcm5hbWUiOiJQYW5rYWogUmFqIiwianRpIjoiZjI3ZGFlOGUtZjEzYy00ZmE2LThmNzYtNTIyNmRmNTViZDU0IiwiZXhwIjoxNjUwMDI4ODY2LCJpc3MiOiJDb21tdW5pY2F0aW9uIFNjYWZmb2xkIn0.yKJnEaIH4VftxmeZ0383rxwGPL2a0wQRRHuJcrF9xuY";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBearerAuth(auth);
		HttpEntity<Object> httpEntity = new HttpEntity<>(videoLink, headers);
		return restTemplate.exchange(baseURL, HttpMethod.POST, httpEntity,
				VideoLinks.class);
	}
	
	public ResponseEntity<Object> getAllVidepoLink(){
		String baseURL="http://localhost:8080/v1/chat/videoLink";
		String auth="eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX0FETUlOIiwiUk9MRV9TVVBFUkFETUlOIiwiUk9MRV9VU0VSIiwiUk9MRV9TVVBFUl9BRE1JTiJdLCJpcCI6IjA6MDowOjA6MDowOjA6MSIsInVzZXJJZCI6MTAxMCwiZW1haWwiOiJwZWV5dXNoLmt1bWFyQG9vZGxlcy5pbyIsInVzZXJuYW1lIjoicGVleXVzaCIsImp0aSI6ImFlZTU0ZDEzLTU5MWMtNGMzZC1iOWIzLTgxNzU0NDBmYjk1MCIsImV4cCI6MTY1MDA2ODEwOCwiaXNzIjoiQ29tbXVuaWNhdGlvbiBTY2FmZm9sZCJ9.GBuIv0lTaLp745oJSHSvdqA2u6BYirrgbgeFyX7vuas";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBearerAuth(auth);
		HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
		VideoLinks links=new VideoLinks();
	System.out.println(links.getLink());
	
	return restTemplate.exchange(baseURL, HttpMethod.GET, httpEntity,
			Object.class);
	}

	//@Scheduled(cron = " 0 06 19 * * *")
	public boolean getTranscribe() {
		System.out.println("inside transcribe");
		List<VideoLinks> videoLinkObj = transcribeRepository.findByIsAudioTranscribe(false);
		if(!videoLinkObj.isEmpty()) {
			for (VideoLinks links : videoLinkObj) {
				String link = links.getLink();
				System.out.println(link);
				String fileName = link.substring(link.lastIndexOf('/') + 1);
				System.out.println(fileName);
				String fileTranscriptionURL = convertAudioToText(fileName);
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
		startTranscriptionJobRequest.withLanguageCode(LanguageCode.EnUS);
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