package com.swift.relay.repository;

import com.swift.relay.entity.OutboxPayment;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentOutboxRepository extends JpaRepository<OutboxPayment,Long> {

  List<OutboxPayment> findTop10ByStatusOrderByCreatedAtAsc(String pending, PageRequest of);
}
