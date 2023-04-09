package com.fmss.logservice.model;


import jakarta.persistence.Id;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@RequiredArgsConstructor
public class LogModel {

  @Id
  private String id;

  private String message;



}

