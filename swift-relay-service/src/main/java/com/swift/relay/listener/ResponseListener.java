package com.swift.relay.listener;

import com.swift.relay.saa.DataPDU;
import com.swift.relay.service.EncryptionService;
import com.swift.relay.service.ProcessResponseService;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import java.io.StringReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ResponseListener {

  private final EncryptionService encryptionService;
  private final ProcessResponseService processResponseService;

  @JmsListener(destination = "SAA.RESPONSE.MQ")
  private void process(String encryptedXml) throws JAXBException {
    try {
      String decryptedXml = encryptionService.decrypt(encryptedXml);
      JAXBContext context = JAXBContext.newInstance(DataPDU.class);
      Unmarshaller unmarshaller = context.createUnmarshaller();

      StringReader reader = new StringReader(decryptedXml);
      DataPDU dataPDU = (DataPDU) unmarshaller.unmarshal(reader);

      String msgId = dataPDU.getHeader().getTransmissionReport().getSenderReference();
      String status = dataPDU.getHeader().getTransmissionReport().getNetworkDeliveryStatus().name();
      processResponseService.processResponse(msgId,status);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

  }
}
