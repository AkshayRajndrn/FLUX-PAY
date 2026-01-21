package com.swift.relay.repository;

import com.swift.relay.entity.OutboxPayment;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PaymentOutboxRepository extends JpaRepository<OutboxPayment,Long> {

//  List<OutboxPayment> findTop10ByStatusOrderByCreatedAtAsc(String pending, PageRequest of);

  @Lock(LockModeType.PESSIMISTIC_WRITE) // This triggers 'FOR UPDATE'
  @QueryHints({@QueryHint(name = "jakarta.persistence.lock.timeout", value = "-2")}) // '-2' is 'SKIP LOCKED'
  @Query("SELECT p FROM OutboxPayment p WHERE p.status = 'PENDING'")
  List<OutboxPayment> findTop10ByStatusOrderByCreatedAtAsc(String status, Pageable pageable);

  Optional<OutboxPayment> findByPaymentId(String msgId);

  @Modifying(clearAutomatically = true)
  @Transactional
  @Query("UPDATE OutboxPayment p SET p.status = :status, p.processedAt = :updateTime WHERE p.paymentId = :paymentId")
  int updateStatusByPaymentId(String status,OffsetDateTime updateTime, String paymentId);

}
