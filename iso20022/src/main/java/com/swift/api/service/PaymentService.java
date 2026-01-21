package com.swift.api.service;

import com.swift.api.dto.PaymentRequest;
import com.swift.api.dto.PaymentResponse;
import com.swift.api.dto.ResponseBody;
import com.swift.api.dto.ResponseHeader;
import com.swift.api.entity.IsoPayments;
import com.swift.api.entity.OutboxPayment;
import com.swift.api.repository.PaymentOutboxRepository;
import com.swift.api.repository.PaymentRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentService {

  private final PaymentRepository paymentRepository;
  private final Pacs009Service pacs009Service;
  private final Pacs008Service pacs008Service;
  private final PaymentOutboxRepository paymentOutboxRepository;

  @Value("${base.bank.bic}")
  String ourBic;

  public ResponseEntity<PaymentResponse> initiatePayment(PaymentRequest request) {
    String e2eId = request.getBody().getEndToEndId();
    String bic = request.getBody().getOrderingBankBic();

    Optional<IsoPayments> existing = paymentRepository.findByOrderingBankBicAndEndToEndId(bic, e2eId);
    if (existing.isPresent()) {
      return ResponseEntity.ok(buildSuccessResponse(request, existing.get(),"Payment Already Processed. Duplicate Entry"));
    }
    try {
      return createNewPaymentInNewTransaction(request);
    } catch (DataIntegrityViolationException ex) {
      return fetchAfterConflict(bic, e2eId, request);
    }
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public ResponseEntity<PaymentResponse> createNewPaymentInNewTransaction(PaymentRequest request) {
    IsoPayments payment = mapToEntity(request);
    payment = paymentRepository.saveAndFlush(payment);
    String xmlMessage = parseToXML(payment);

    OutboxPayment outbox = OutboxPayment.builder()
        .paymentId(payment.getId())
        .payload(xmlMessage)
        .status("PENDING")
        .build();
    paymentOutboxRepository.save(outbox);
    return ResponseEntity.ok(buildSuccessResponse(request, payment,"Payment Received Successfully"));
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
  public ResponseEntity<PaymentResponse> fetchAfterConflict(String bic, String e2eId, PaymentRequest request) {
    return paymentRepository.findByOrderingBankBicAndEndToEndId(bic, e2eId)
        .map(existing -> ResponseEntity.ok(buildSuccessResponse(request, existing,"Payment with EndToEndId [ID] already exists.")))
        .orElseThrow(() -> new RuntimeException("Conflict detected but record still missing."));
  }


  private String parseToXML(IsoPayments request) {
    String messageType = request.getMessageType();
    if ("pacs.008".equalsIgnoreCase(messageType)) {
      return pacs008Service.generatePacs008(request);
    } else if ("pacs.009".equalsIgnoreCase(messageType)) {
      return null;
    } else {
      throw new IllegalArgumentException("Unsupported message type: " + messageType);
    }
  }


  private IsoPayments mapToEntity(PaymentRequest request) {
    var body = request.getBody();
    String expectedType = body.getValueDate().isAfter(LocalDate.now())
        ? "FUTURE_DAY" : "SAME_DAY";
    return IsoPayments.builder()
        .endToEndId(body.getEndToEndId())
        .transactionReference(body.getTransactionReference())
        .amount(body.getAmount())
        .currency(body.getCurrency())
        .status("RCVD")
        .orderingCustName(body.getOrderingCustName())
        .benefCustName(body.getBenefCustName())
        .benefAccNum(body.getBenefAccNum())
        .orderingAccNum(body.getOrderingAccNum())
        .benefBankBic(body.getBenefBankBic())
        .orderingBankBic(ourBic)
        .messageType(body.getMessageType())
        .valueDate(body.getValueDate())
        .creationDate(OffsetDateTime.now())
        .purposeCode(body.getPurposeCode())
        .chargeBearer(body.getChargeBearer())
        .paymentType(expectedType)
        .build();
  }

  private PaymentResponse buildSuccessResponse(PaymentRequest request, IsoPayments saved,String message) {
    return PaymentResponse.builder()
        .header(ResponseHeader.builder()
            .requestId(request.getHeader().getRequestId())
            .messageId(UUID.randomUUID().toString())
            .timestamp(LocalDateTime.now().toString())
            .status("SUCCESS")
            .statusCode("200")
            .build())
        .body(ResponseBody.builder()
            .uetr(saved.getUetr().toString())
            .paymentStatus(saved.getStatus())
            .message(message)
            .build())
        .build();
  }

}


