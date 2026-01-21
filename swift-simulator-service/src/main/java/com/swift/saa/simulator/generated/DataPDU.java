package com.swift.saa.simulator.generated;

import com.swift.saa.generated.Header;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "",
    propOrder = {"revision", "header"}
)
@XmlRootElement(
    name = "DataPDU"
)
public class DataPDU {
  @XmlElement(
      name = "Revision"
  )
  protected String revision;
  @XmlElement(
      name = "Header",
      required = true
  )
  protected Header header;

  public DataPDU() {
  }

  public String getRevision() {
    return this.revision;
  }

  public void setRevision(String value) {
    this.revision = value;
  }

  public Header getHeader() {
    return this.header;
  }

  public void setHeader(Header value) {
    this.header = value;
  }
}
