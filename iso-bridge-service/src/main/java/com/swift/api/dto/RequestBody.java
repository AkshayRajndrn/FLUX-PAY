package com.swift.api.dto;


import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestBody {

  @NotBlank(message = "End-to-End ID is mandatory")
  @Size(max = 35)
  private String endToEndId;

  @NotBlank(message = "Transaction Reference is mandatory")
  @Size(max = 35)
  private String transactionReference;

  @NotNull(message = "Amount is mandatory")
  @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
  private BigDecimal amount;

  @NotBlank(message = "Currency is mandatory")
  @Size(min = 3, max = 3, message = "Currency must be a 3-letter ISO code")
  private String currency;

  @NotNull(message = "Value Date is mandatory")
  private LocalDate valueDate;

  @NotBlank(message = "Ordering Customer Name is mandatory")
  private String orderingCustName;

  @NotBlank(message = "Ordering Account Number is mandatory")
  private String orderingAccNum;

  private String orderingBankBic;

  // Creditor (Beneficiary Party)
  @NotBlank(message = "Beneficiary Customer Name is mandatory")
  private String benefCustName;

  @NotBlank(message = "Beneficiary Account Number is mandatory")
  private String benefAccNum;

  private String benefBankBic;

  @NotBlank(message = "Message Type is mandatory")
  private String messageType;

  private String chargeBearer; // SHAR, DEBT, CRED

  private String purposeCode;

}
