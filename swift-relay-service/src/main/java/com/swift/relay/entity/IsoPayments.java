package com.swift.relay.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "iso_payments", uniqueConstraints = {
        @UniqueConstraint(name = "ux_iso_payment_idempotency", columnNames = {"ordering_bank_bic", "end_to_end_id"})}
)
public class IsoPayments {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, updatable = false)
  @Builder.Default
  private UUID uetr = UUID.randomUUID();

  @Column(name = "end_to_end_id", length = 35, nullable = false)
  private String endToEndId;

  @Column(name = "transaction_reference", length = 35)
  private String transactionReference;

  @Column(name = "creation_date", updatable = false)
  @Builder.Default
  private OffsetDateTime creationDate = OffsetDateTime.now();

  @Column(name = "updated_at", updatable = false)
  @Builder.Default
  private OffsetDateTime updatedTime = OffsetDateTime.now();

  @Column(name = "value_date", nullable = false)
  private LocalDate valueDate;

  @Column(name = "payment_type")
  private String paymentType;

  @Column(precision = 18, scale = 2, nullable = false)
  private BigDecimal amount;

  @Column(length = 3, nullable = false)
  private String currency;

  @Column(name = "charge_bearer", length = 4)
  private String chargeBearer;

  @Column(name = "ordering_cust_name", length = 140, nullable = false)
  private String orderingCustName;

  @Column(name = "ordering_acc_num", length = 34, nullable = false)
  private String orderingAccNum;

  @Column(name = "ordering_bank_bic", length = 11)
  private String orderingBankBic;

  @Column(name = "benef_cust_name", length = 140, nullable = false)
  private String benefCustName;

  @Column(name = "benef_acc_num", length = 34, nullable = false)
  private String benefAccNum;

  @Column(name = "benef_bank_bic", length = 11)
  private String benefBankBic;

  @Column(name = "purpose_code", length = 4)
  private String purposeCode;

  @Column(name ="message_type")
  private String messageType;

  @Column(name = "status")
  @Builder.Default
  private String status = "RCVD";


}
