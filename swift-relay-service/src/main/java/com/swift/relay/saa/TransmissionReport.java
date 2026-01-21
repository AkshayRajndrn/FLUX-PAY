package com.swift.relay.saa;


import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "TransmissionReport",
    propOrder = {"senderReference", "networkDeliveryStatus"}
)
public class TransmissionReport {
  @XmlElement(
      name = "SenderReference",
      required = true
  )
  protected String senderReference;
  @XmlElement(
      name = "NetworkDeliveryStatus",
      required = true
  )
  @XmlSchemaType(
      name = "string"
  )
  protected DeliveryStatus networkDeliveryStatus;

  public TransmissionReport() {
  }

  public String getSenderReference() {
    return this.senderReference;
  }

  public void setSenderReference(String value) {
    this.senderReference = value;
  }

  public DeliveryStatus getNetworkDeliveryStatus() {
    return this.networkDeliveryStatus;
  }

  public void setNetworkDeliveryStatus(DeliveryStatus value) {
    this.networkDeliveryStatus = value;
  }
}
