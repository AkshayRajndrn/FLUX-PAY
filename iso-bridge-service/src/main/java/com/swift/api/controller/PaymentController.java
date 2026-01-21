package com.swift.api.controller;

import com.swift.api.dto.PaymentRequest;
import com.swift.api.dto.PaymentResponse;
import com.swift.api.service.PaymentService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {

  private final PaymentService paymentService;

  @PostMapping("/initiate")
  public ResponseEntity<PaymentResponse>initiatePayment(@RequestBody PaymentRequest request){
    if(request.getHeader().getRequestId()==null){
      request.getHeader().setRequestId(UUID.randomUUID().toString());
    }
    return paymentService.initiatePayment(request);
  }

}
