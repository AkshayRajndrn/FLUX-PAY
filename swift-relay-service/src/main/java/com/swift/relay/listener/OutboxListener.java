package com.swift.relay.listener;

import com.swift.relay.entity.OutboxPayment;
import com.swift.relay.repository.PaymentOutboxRepository;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
@RequiredArgsConstructor
public class OutboxListener {

  private final PaymentOutboxRepository paymentOutboxRepository;
  private final JmsTemplate jmsTemplate;

  @Value("${saa.payment.mq}")
  String saaMQ;

  @Transactional
  @Scheduled(fixedDelayString = "${relay.polling.interval:2000}")
  public void publish() {

    List<OutboxPayment> pendingMessages = paymentOutboxRepository.findTop10ByStatusOrderByCreatedAtAsc(
        "PENDING", PageRequest.of(0, 10)
    );


    for (OutboxPayment message : pendingMessages) {
      try {
        String xml = message.getPayload();
        jmsTemplate.convertAndSend(saaMQ, xml);
        message.setStatus("SENT_TO_SAA");
        OffsetDateTime.now();
        message.setProcessedAt(OffsetDateTime.now());
        paymentOutboxRepository.save(message);
        log.info("Successfully pushed payment {} to ActiveMQ", message.getPaymentId());
      } catch (Exception e) {
        message.setStatus("FAILED");
        message.setLastError(e.getMessage());
        log.error("Failed to push payment {}", message.getPaymentId(), e);
      }
    }
  }
}


