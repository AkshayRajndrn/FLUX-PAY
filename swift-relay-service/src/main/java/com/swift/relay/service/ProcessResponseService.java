package com.swift.relay.service;

import com.swift.relay.entity.OutboxPayment;
import com.swift.relay.repository.PaymentOutboxRepository;

import com.swift.relay.repository.PaymentRepository;
import jakarta.transaction.Transactional;
import java.time.OffsetDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProcessResponseService {

  private final PaymentOutboxRepository paymentOutboxRepository;
  private final PaymentRepository paymentRepository;

  @Transactional
  public void processResponse(String msgId, String status) {

    OutboxPayment payment = paymentOutboxRepository.findByPaymentId(msgId)
        .orElseThrow(() -> new NoSuchElementException("Payment with ID " + msgId + " not found"));

    log.info("Payment will be Updated : {}",payment.getPaymentId());

    int updatedRows = paymentOutboxRepository.updateStatusByPaymentId(status,
        OffsetDateTime.now(), msgId);

    int isoUpdated = paymentRepository.updateFinancialStatus(status, OffsetDateTime.now(), msgId);

    if (updatedRows == 0 || isoUpdated == 0) {
      log.warn("Update failed: Payment ID {} not found in Database.", msgId);
      throw new NoSuchElementException("Payment ID not found: " + msgId);

    }
    log.info("Outbox updated: Payment {} is now {}", msgId, status);



  }
}
