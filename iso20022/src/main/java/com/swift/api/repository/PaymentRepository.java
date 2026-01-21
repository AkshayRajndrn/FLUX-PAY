package com.swift.api.repository;

import com.swift.api.entity.IsoPayments;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<IsoPayments,Long> {

  Optional<IsoPayments> findByOrderingBankBicAndEndToEndId(String orderingBankBic, String endToEndId);
}
