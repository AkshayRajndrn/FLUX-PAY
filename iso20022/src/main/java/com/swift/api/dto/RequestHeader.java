package com.swift.api.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestHeader {

  private String requestId;
  private String sourceSystem;
  private String timestamp;
  private String channelId;

}
