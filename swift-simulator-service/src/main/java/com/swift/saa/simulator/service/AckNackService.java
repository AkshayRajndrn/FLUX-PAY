package com.swift.saa.simulator.service;

import com.swift.saa.generated.DataPDU;
import com.swift.saa.generated.DeliveryStatus;
import com.swift.saa.generated.Header;
import com.swift.saa.generated.TransmissionReport;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import java.io.StringWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AckNackService {

  private final JmsTemplate jmsTemplate;
  private final EncryptionService encryptionService;

  @Value("${saa.response.mq}")
  private String responseMQ;

  public void processAckNackMessage(String msgId, String status) {
    try{

      TransmissionReport trReport = new TransmissionReport();
      trReport.setSenderReference(msgId);
      trReport.setNetworkDeliveryStatus(
          status.equalsIgnoreCase("NetworkNacked") ? DeliveryStatus.NETWORK_NACKED : DeliveryStatus.NETWORK_ACKED);

      Header header = new Header();
      header.setTransmissionReport(trReport);

      DataPDU dataPDU = new DataPDU();
      dataPDU.setHeader(header);

      JAXBContext context = JAXBContext.newInstance(DataPDU.class);
      Marshaller marshaller = context.createMarshaller();
      marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

      StringWriter sw = new StringWriter();
      marshaller.marshal(dataPDU, sw);
      String plainXml = sw.toString();

      log.info("Generated Response XML :{}",plainXml);

      String encryptedXml = encryptionService.encrypt(plainXml);

      jmsTemplate.convertAndSend(responseMQ, encryptedXml);

    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
