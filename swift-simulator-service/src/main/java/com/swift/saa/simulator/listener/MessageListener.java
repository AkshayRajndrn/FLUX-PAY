package com.swift.saa.simulator.listener;

import com.swift.saa.simulator.service.SwiftSimulatorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class MessageListener {

  private final SwiftSimulatorService swiftSimulatorService;

  @Value("${payment.out.queue}")
  String outMq;

  @JmsListener(destination = "PAYMENT.OUT.QUEUE")
  public void simulateNetworkResponse(String xmlPayload) throws NoSuchMethodException {
    log.info("SAA received message from Relay. Performing Network Validation...");
    swiftSimulatorService.processMessage(xmlPayload);
  }
}
