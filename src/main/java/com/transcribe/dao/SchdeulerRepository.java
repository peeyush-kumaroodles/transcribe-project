package com.transcribe.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.transcribe.scheduler.dto.JobDetailRequestBean;

@Repository
public interface SchdeulerRepository extends MongoRepository<JobDetailRequestBean, String>{

}
