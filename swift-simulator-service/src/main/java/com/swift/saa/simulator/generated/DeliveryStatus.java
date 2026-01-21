package com.swift.saa.simulator.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;

@XmlType(
    name = "DeliveryStatus"
)
@XmlEnum
public enum DeliveryStatus {
  @XmlEnumValue("NetworkAcked")
  NETWORK_ACKED("NetworkAcked"),
  @XmlEnumValue("NetworkNacked")
  NETWORK_NACKED("NetworkNacked"),
  @XmlEnumValue("DeliveryAborted")
  DELIVERY_ABORTED("DeliveryAborted");

  private final String value;

  private DeliveryStatus(String v) {
    this.value = v;
  }

  public String value() {
    return this.value;
  }

  public static DeliveryStatus fromValue(String v) {
    com.swift.saa.simulator.generated.DeliveryStatus[] var1 = values();
    int var2 = var1.length;

    for(int var3 = 0; var3 < var2; ++var3) {
      com.swift.saa.simulator.generated.DeliveryStatus c = var1[var3];
      if (c.value.equals(v)) {
        return c;
      }
    }

    throw new IllegalArgumentException(v);
  }
}
