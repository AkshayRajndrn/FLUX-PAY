package com.swift.relay.repository;

import com.swift.relay.entity.IsoPayments;
import java.time.OffsetDateTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PaymentRepository extends JpaRepository<IsoPayments,Long> {

  Optional<IsoPayments> findByOrderingBankBicAndEndToEndId(String orderingBankBic, String endToEndId);

  @Modifying(clearAutomatically = true)
  @Transactional
  @Query("UPDATE IsoPayments p SET p.status = :status, p.updatedTime = :updatedTime WHERE p.endToEndId = :paymentId")
  int updateFinancialStatus(String status,OffsetDateTime updatedTime, String paymentId);
}
