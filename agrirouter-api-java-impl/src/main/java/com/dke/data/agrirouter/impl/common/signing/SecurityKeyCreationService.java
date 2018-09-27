package com.dke.data.agrirouter.impl.common.signing;

import com.dke.data.agrirouter.api.exception.CouldNotCreatePrivateKeyException;
import com.dke.data.agrirouter.api.exception.CouldNotCreatePublicKeyException;
import com.dke.data.agrirouter.api.service.LoggingEnabledService;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class SecurityKeyCreationService implements LoggingEnabledService {

  private String privateKey;
  private String publicKey;

  public PrivateKey createPrivateKey(String privateKey) {
    this.logMethodBegin(privateKey);

    this.privateKey = privateKey;
    PrivateKey result;
    try {
      result = this.generateValidPrivateKey();
    } catch (IllegalArgumentException | InvalidKeySpecException | NoSuchAlgorithmException e) {
      throw new CouldNotCreatePrivateKeyException(e);
    }

    this.logMethodEnd(result);

    return result;
  }

  public PublicKey createPublicKey(String publicKey) {
    this.logMethodBegin(publicKey);

    this.publicKey = publicKey;
    PublicKey result;
    try {
      result = this.generateValidPublicKey();
    } catch (IllegalArgumentException | InvalidKeySpecException | NoSuchAlgorithmException e) {
      throw new CouldNotCreatePublicKeyException(e);
    }

    this.logMethodEnd(result);

    return result;
  }

  private PublicKey generateValidPublicKey() throws InvalidKeySpecException, NoSuchAlgorithmException {
    String pkcs8Pem = this.replaceCommentsInPublic();
    byte[] pkcs8EncodedBytes = this.getEncodedBytes(pkcs8Pem);

    this.getNativeLogger().trace("Generate public key.");
    X509EncodedKeySpec keySpec = new X509EncodedKeySpec(pkcs8EncodedBytes);
    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
    return keyFactory.generatePublic(keySpec);
  }

  private PrivateKey generateValidPrivateKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
    String pkcs8Pem = this.replaceCommentsInPrivate();
    byte[] pkcs8EncodedBytes = this.getEncodedBytes(pkcs8Pem);

    this.getNativeLogger().trace("Generate private key.");
    PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(pkcs8EncodedBytes);
    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
    return keyFactory.generatePrivate(keySpec);
  }

  private String replaceCommentsInPublic() {
    return this.replaceComments(publicKey, "public");
  }

  private String replaceCommentsInPrivate() {
    return this.replaceComments(privateKey, "private");
  }

  private String replaceComments(String key, String keyType) {
    this.getNativeLogger().trace("Replacing comments within file.");
    
    String pkcs8Pem = key.replace("-----BEGIN " + keyType.toUpperCase() + " KEY-----", "");
    pkcs8Pem = pkcs8Pem.replace("-----END " + keyType.toUpperCase() + " KEY-----", "");
    pkcs8Pem = pkcs8Pem.replaceAll("\\s+", "");
    return pkcs8Pem;
  }

  private byte[] getEncodedBytes(String pkcs8Pem) {
    this.getNativeLogger().trace("Decode base 64 values.");
    return Base64.getDecoder().decode(pkcs8Pem);
  }
}
