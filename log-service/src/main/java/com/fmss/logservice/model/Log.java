package com.fmss.logservice.model;


import jakarta.persistence.Id;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "log_db")
@RequiredArgsConstructor
public class Log {

  @Id
  private String id;

  private String message;
}

