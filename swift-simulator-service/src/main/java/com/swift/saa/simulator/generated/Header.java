package com.swift.saa.simulator.generated;

import com.swift.saa.generated.TransmissionReport;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "Header",
    propOrder = {"transmissionReport"}
)
public class Header {
  @XmlElement(
      name = "TransmissionReport",
      required = true
  )
  protected TransmissionReport transmissionReport;

  public Header() {
  }

  public TransmissionReport getTransmissionReport() {
    return this.transmissionReport;
  }

  public void setTransmissionReport(TransmissionReport value) {
    this.transmissionReport = value;
  }
}
