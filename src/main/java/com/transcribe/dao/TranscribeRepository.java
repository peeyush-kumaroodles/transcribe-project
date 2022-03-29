package com.transcribe.dao;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.transcribe.dto.VideoLinks;
@Repository
public interface TranscribeRepository extends MongoRepository<VideoLinks, String>{
	List<VideoLinks> findByIsAudioTranscribe(boolean isAudioTranscribe);
}
