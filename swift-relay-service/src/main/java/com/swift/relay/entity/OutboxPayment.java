package com.swift.relay.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "payment_outbox")
@Builder
public class OutboxPayment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "payment_id", nullable = false)
  private Long paymentId;

  @Column(columnDefinition = "TEXT", nullable = false)
  private String payload;

  @Builder.Default
  @Column(nullable = false, length = 20)
  private String status = "PENDING";

  @Builder.Default
  @Column(name = "retry_count")
  private Integer retryCount = 0;

  @Column(name = "last_error")
  private String lastError;

  @Builder.Default
  @Column(name = "created_at", updatable = false)
  private OffsetDateTime createdAt = OffsetDateTime.now();

  @Column(name = "processed_at")
  private OffsetDateTime processedAt;
}
