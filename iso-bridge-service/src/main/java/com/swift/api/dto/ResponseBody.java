package com.swift.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseBody {

  private String uetr;
  private String endToEndId;
  private String paymentStatus;
  private String message;
  private String creationDate;

}
