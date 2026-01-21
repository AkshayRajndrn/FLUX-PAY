package com.swift.saa.simulator.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class MessageListener {

  @JmsListener(destination = "PAYMENT.OUT.QUEUE")
  @SendTo("PAYMENT.RESPONSE.QUEUE")
  public String simulateNetworkResponse(String xmlPayload) {
    log.info("SAA received message from Relay. Performing Network Validation...");

    // Simulate a Network NACK if the XML is missing a required tag (like <BizMsgHdr>)
    if (!xmlPayload.contains("<AppHdr") || !xmlPayload.contains("<Document")) {
      log.warn("Network NACK: Invalid ISO Structure");
      return "NETWORK_NACK: INVALID_STRUCTURE";
    }

    // Simulate a successful Network ACK
    log.info("Network ACK: Message accepted by SWIFT Network");
    return "NETWORK_ACK";
  }
}
