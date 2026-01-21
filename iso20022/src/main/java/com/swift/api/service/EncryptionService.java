package com.swift.api.service;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EncryptionService {

  @Value("${app.security.encryption-key}")
  private String secretKey;

  private static final String ALGORITHM = "AES";

  public String encrypt(String strToEncrypt) {
    try {
      SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), ALGORITHM);
      Cipher cipher = Cipher.getInstance(ALGORITHM);
      cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
      return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes()));
    } catch (Exception e) {
      throw new RuntimeException("Error while encrypting: " + e.toString());
    }
  }

  public String decrypt(String strToDecrypt) {
    try {
      SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), ALGORITHM);
      Cipher cipher = Cipher.getInstance(ALGORITHM);
      cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
      return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
    } catch (Exception e) {
      throw new RuntimeException("Error while decrypting: " + e.toString());
    }
  }

}
