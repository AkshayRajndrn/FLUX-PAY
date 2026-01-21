package com.swift.api.service;
import com.prowidesoftware.swift.model.mx.MxPacs00800108;
import com.prowidesoftware.swift.model.mx.MxPacs00800109;
import com.prowidesoftware.swift.model.mx.dic.AccountIdentification4Choice;
import com.prowidesoftware.swift.model.mx.dic.ActiveCurrencyAndAmount;
import com.prowidesoftware.swift.model.mx.dic.BranchAndFinancialInstitutionIdentification6;
import com.prowidesoftware.swift.model.mx.dic.CashAccount38;
import com.prowidesoftware.swift.model.mx.dic.ChargeBearerType1Code;
import com.prowidesoftware.swift.model.mx.dic.CreditTransferTransaction39;
import com.prowidesoftware.swift.model.mx.dic.CreditTransferTransaction43;
import com.prowidesoftware.swift.model.mx.dic.FIToFICustomerCreditTransferV08;
import com.prowidesoftware.swift.model.mx.dic.FIToFICustomerCreditTransferV09;
import com.prowidesoftware.swift.model.mx.dic.FinancialInstitutionIdentification18;
import com.prowidesoftware.swift.model.mx.dic.GenericAccountIdentification1;
import com.prowidesoftware.swift.model.mx.dic.GroupHeader93;
import com.prowidesoftware.swift.model.mx.dic.PartyIdentification135;
import com.prowidesoftware.swift.model.mx.dic.PaymentIdentification13;
import com.prowidesoftware.swift.model.mx.dic.PaymentIdentification7;
import com.prowidesoftware.swift.model.mx.dic.PaymentTypeInformation28;
import com.prowidesoftware.swift.model.mx.dic.PostalAddress24;
import com.prowidesoftware.swift.model.mx.dic.ServiceLevel8Choice;
import com.prowidesoftware.swift.model.mx.dic.SettlementInstruction7;
import com.prowidesoftware.swift.model.mx.dic.SettlementMethod1Code;
import com.swift.api.entity.IsoPayments;
import java.io.StringReader;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Objects;
import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tools.jackson.databind.ObjectMapper;


@Service
@Slf4j
@RequiredArgsConstructor
public class Pacs008Service {

  private final ObjectMapper objectMapper;

  public String generatePacs008(IsoPayments request) {

    Objects.requireNonNull(request, "Request body cannot be null");

    MxPacs00800109 pacs008 = new MxPacs00800109();

    log.info(objectMapper.writeValueAsString(request));

    // 1. Group Header (Mandatory)
    FIToFICustomerCreditTransferV09 fi = new FIToFICustomerCreditTransferV09();
    fi.setGrpHdr(buildGroupHeader(request));


    // 2. Transaction Information
    CreditTransferTransaction43 creditTransferTransaction43 = new CreditTransferTransaction43 ();

    PaymentIdentification13 paymentIdentification13 = new PaymentIdentification13();
    paymentIdentification13.setEndToEndId(request.getEndToEndId());
    paymentIdentification13.setUETR(String.valueOf(request.getUetr()));

    creditTransferTransaction43.setPmtId(paymentIdentification13);
    creditTransferTransaction43.setPmtTpInf(new PaymentTypeInformation28().addSvcLvl(new ServiceLevel8Choice().setCd("G001")));

    ActiveCurrencyAndAmount settlementAmount = new ActiveCurrencyAndAmount();
    settlementAmount.setCcy(request.getCurrency());
    settlementAmount.setValue(request.getAmount().setScale(2, RoundingMode.CEILING));
    creditTransferTransaction43.setIntrBkSttlmAmt(settlementAmount);

    if(StringUtils.hasLength(String.valueOf(request.getValueDate()))){
      creditTransferTransaction43.setIntrBkSttlmDt(request.getValueDate());
    }

    creditTransferTransaction43.setChrgBr(ChargeBearerType1Code.fromValue(request.getChargeBearer()));

    if(StringUtils.hasLength(request.getOrderingBankBic())){
      BranchAndFinancialInstitutionIdentification6 instrtgAgt = new BranchAndFinancialInstitutionIdentification6();
      FinancialInstitutionIdentification18 finInstnId1 = new FinancialInstitutionIdentification18();
      finInstnId1.setBICFI(request.getOrderingBankBic());
      instrtgAgt.setFinInstnId(finInstnId1);
      creditTransferTransaction43.setInstgAgt(instrtgAgt);
    }

    if(StringUtils.hasLength(request.getBenefBankBic())){
      BranchAndFinancialInstitutionIdentification6 instrtdAgt = new BranchAndFinancialInstitutionIdentification6();
      FinancialInstitutionIdentification18 finInstnId2 = new FinancialInstitutionIdentification18();
      finInstnId2.setBICFI(request.getBenefBankBic());
      instrtdAgt.setFinInstnId(finInstnId2);
      creditTransferTransaction43.setInstdAgt(instrtdAgt);
    }

    PartyIdentification135 dbtr = new PartyIdentification135();
    PostalAddress24 dbtrPstlAdr = new PostalAddress24();

    if(StringUtils.hasLength(request.getOrderingCustName())) {
      dbtr.setNm(request.getOrderingCustName());
    }
    creditTransferTransaction43.setDbtr(dbtr);

    if(request.getOrderingAccNum().matches("[A-Z]{2,2}[0-9]{2,2}[a-zA-Z0-9]{1,30}")) {
      creditTransferTransaction43.setDbtrAcct(new CashAccount38().setId(new AccountIdentification4Choice().setIBAN(request.getOrderingAccNum())));
    } else {
      creditTransferTransaction43.setDbtrAcct(new CashAccount38().setId(new AccountIdentification4Choice().setOthr(new GenericAccountIdentification1().setId(request.getOrderingAccNum()))));
    }

    if(StringUtils.hasLength(request.getOrderingBankBic())){
      BranchAndFinancialInstitutionIdentification6 dbtrAgt = new BranchAndFinancialInstitutionIdentification6();
      FinancialInstitutionIdentification18 dbtrFinInstnId = new FinancialInstitutionIdentification18();

      dbtrFinInstnId.setBICFI(request.getOrderingBankBic());
      dbtrAgt.setFinInstnId(dbtrFinInstnId);
      creditTransferTransaction43.setDbtrAgt(dbtrAgt);
    }

    if(StringUtils.hasLength(request.getBenefCustName())){
      PartyIdentification135 cdtr = new PartyIdentification135();
      PostalAddress24 cdtrPstlAdr = new PostalAddress24();
      if(StringUtils.hasLength(request.getBenefCustName())) {
        cdtr.setNm(request.getBenefCustName());
      }
      creditTransferTransaction43.setCdtr(cdtr);
    }



    if(request.getBenefAccNum().matches("[A-Z]{2,2}[0-9]{2,2}[a-zA-Z0-9]{1,30}")) {
      creditTransferTransaction43.setCdtrAcct(new CashAccount38().setId(new AccountIdentification4Choice().setIBAN(request.getBenefAccNum())));
    } else {
      creditTransferTransaction43.setCdtrAcct(new CashAccount38().setId(new AccountIdentification4Choice().setOthr(new GenericAccountIdentification1().setId(request.getBenefAccNum()))));
    }

    if(StringUtils.hasLength(request.getBenefBankBic())){
      BranchAndFinancialInstitutionIdentification6 cdtrAgt = new BranchAndFinancialInstitutionIdentification6();
      FinancialInstitutionIdentification18 cdtrFinInstnId = new FinancialInstitutionIdentification18();

      cdtrFinInstnId.setBICFI(request.getBenefBankBic());
      cdtrAgt.setFinInstnId(cdtrFinInstnId);
      creditTransferTransaction43.setCdtrAgt(cdtrAgt);
    }

    fi.addCdtTrfTxInf(creditTransferTransaction43);
    pacs008.setFIToFICstmrCdtTrf(fi);


    log.info("Generated pacs008 Message: {}", pacs008.message());

   boolean isValid = isValidXsd(pacs008.message());
    if(!isValid){
      return "Invalid XML";
    }
    return pacs008.message();

  }

  private GroupHeader93 buildGroupHeader(IsoPayments req) {
    GroupHeader93 hdr = new GroupHeader93();
    hdr.setMsgId(req.getEndToEndId());
    hdr.setCreDtTm(OffsetDateTime.now());
    hdr.setNbOfTxs("1");
    // Settlement Method (Mandatory)
    hdr.setSttlmInf(new SettlementInstruction7().setSttlmMtd(SettlementMethod1Code.INDA));
    return hdr;
  }

  private boolean isValidXsd(String xml) {
    try {
      // This uses the standard Java validation engine
      SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

      // Note: You need the pacs.008.001.09.xsd file in your src/main/resources/schemas folder
      Schema schema = factory.newSchema(new StreamSource(
          getClass().getResourceAsStream("/schemas/pacs.008.001.09.xsd")
      ));

      schema.newValidator().validate(new StreamSource(new StringReader(xml)));
      log.info("XSD Validation Successful");
      return true;

    } catch (Exception e) {
      log.error("XSD Validation Failed: {}", e.getMessage());
      throw new IllegalArgumentException("XML Schema Error: " + e.getMessage());
    }
  }
}
