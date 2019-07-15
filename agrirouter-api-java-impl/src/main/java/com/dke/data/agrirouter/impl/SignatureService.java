package com.dke.data.agrirouter.impl;

import static java.nio.charset.StandardCharsets.UTF_8;

import com.dke.data.agrirouter.api.exception.InvalidSignatureException;
import com.dke.data.agrirouter.impl.common.signing.SecurityKeyCreationService;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.SignatureException;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

public interface SignatureService {
  String SIGNATURE_ALGORITHM = "SHA256withRSA";

  default byte[] createSignature(String requestBody, String privateKey) {
    try {
      SecurityKeyCreationService securityKeyCreationService = new SecurityKeyCreationService();
      Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
      signature.initSign(securityKeyCreationService.createPrivateKey(privateKey));
      signature.update(requestBody.getBytes(UTF_8));
      return signature.sign();
    } catch (NoSuchAlgorithmException | SignatureException | InvalidKeyException e) {
      throw new RuntimeException(e);
    }
  }

  default void verifySignature(String requestBody, byte[] signedBytes, String publicKey) {
    try {
      SecurityKeyCreationService securityKeyCreationService = new SecurityKeyCreationService();
      Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
      signature.initVerify(securityKeyCreationService.createPublicKey(publicKey));
      signature.update(requestBody.getBytes(UTF_8));
      if (!signature.verify(signedBytes)) {
        throw new InvalidSignatureException();
      }
    } catch (NoSuchAlgorithmException | SignatureException | InvalidKeyException e) {
      throw new RuntimeException(e);
    }
  }

  default byte[] decodeHex(String encodedSignature) {
    try {
      return Hex.decodeHex(encodedSignature.toCharArray());
    } catch (DecoderException e) {
      throw new RuntimeException(e);
    }
  }
}
