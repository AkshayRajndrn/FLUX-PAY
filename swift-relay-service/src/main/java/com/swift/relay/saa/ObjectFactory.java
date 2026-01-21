package com.swift.relay.saa;

import com.swift.relay.saa.DataPDU;
import com.swift.relay.saa.Header;
import com.swift.relay.saa.TransmissionReport;
import jakarta.xml.bind.annotation.XmlRegistry;

@XmlRegistry
public class ObjectFactory {
  public ObjectFactory() {
  }

  public com.swift.relay.saa.DataPDU createDataPDU() {
    return new DataPDU();
  }

  public com.swift.relay.saa.Header createHeader() {
    return new Header();
  }

  public TransmissionReport createTransmissionReport() {
    return new TransmissionReport();
  }
}
