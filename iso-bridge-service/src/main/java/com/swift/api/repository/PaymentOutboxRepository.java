package com.swift.api.repository;

import com.swift.api.entity.OutboxPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentOutboxRepository extends JpaRepository<OutboxPayment,Long> {

}
