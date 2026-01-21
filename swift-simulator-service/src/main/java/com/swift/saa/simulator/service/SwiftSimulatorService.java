package com.swift.saa.simulator.service;

import com.prowidesoftware.swift.model.mx.MxPacs00800109;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import java.io.StringReader;
import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class SwiftSimulatorService {

  private final EncryptionService encryptionService;
  private final AckNackService ackNackService;

  public void processMessage(String xmlPayload) throws NoSuchMethodException {

    //      decrypt the txt
    String decryptedXml = encryptionService.decrypt(xmlPayload);

    String cleanedXml = decryptedXml.replaceAll("<Doc:", "<").replaceAll("</Doc:", "</");
    MxPacs00800109 pacs008 = MxPacs00800109.parse(cleanedXml);
    if (pacs008 == null || pacs008.getFIToFICstmrCdtTrf() == null) {
      log.error("Failed to parse Pacs.008 content. Object is null.");
      throw new NoSuchMethodException();
    }
    String msgId = pacs008.getFIToFICstmrCdtTrf().getGrpHdr().getMsgId();
    log.info("Successfully unmarshalled Message ID: {}", msgId);

    if(isValidXsd(pacs008.message())){
       ackNackService.processAckNackMessage(msgId,"NetworkAcked");
    }else{
      ackNackService.processAckNackMessage(msgId,"NetworkNacked");
    }

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
