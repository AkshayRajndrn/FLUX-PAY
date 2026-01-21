package com.swift.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseHeader {

  private String requestId;
  private String messageId;
  private String timestamp;
  private String status;
  private String statusCode;

}
