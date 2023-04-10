package com.fmss.logservice.model;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class LogModel {

  @Id
  private String id;

  private String message;


}

