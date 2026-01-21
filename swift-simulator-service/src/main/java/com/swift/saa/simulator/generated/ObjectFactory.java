package com.swift.saa.simulator.generated;

import com.swift.saa.generated.DataPDU;
import com.swift.saa.generated.Header;
import com.swift.saa.generated.TransmissionReport;
import jakarta.xml.bind.annotation.XmlRegistry;

@XmlRegistry
public class ObjectFactory {
  public ObjectFactory() {
  }

  public com.swift.saa.generated.DataPDU createDataPDU() {
    return new DataPDU();
  }

  public com.swift.saa.generated.Header createHeader() {
    return new Header();
  }

  public TransmissionReport createTransmissionReport() {
    return new TransmissionReport();
  }
}
