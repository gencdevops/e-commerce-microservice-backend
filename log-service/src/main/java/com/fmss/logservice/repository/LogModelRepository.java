package com.fmss.logservice.repository;

import com.fmss.logservice.model.LogModel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LogModelRepository extends MongoRepository<LogModel, String> {
}
