package com.dke.data.agrirouter.impl;

import com.dke.data.agrirouter.api.exception.CouldNotCreateSignatureException;
import com.dke.data.agrirouter.api.exception.CouldNotDecodeHexException;
import com.dke.data.agrirouter.api.exception.CouldNotVerifySignatureException;
import com.dke.data.agrirouter.api.exception.InvalidSignatureException;
import com.dke.data.agrirouter.impl.common.signing.SecurityKeyCreationService;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.SignatureException;

import static java.nio.charset.StandardCharsets.UTF_8;

public interface SignatureService {
    String SIGNATURE_ALGORITHM = "SHA256withRSA";

    default byte[] createSignature(String requestBody, String privateKey) {
        try {
            var securityKeyCreationService = new SecurityKeyCreationService();
            var signature = Signature.getInstance(SIGNATURE_ALGORITHM);
            signature.initSign(securityKeyCreationService.createPrivateKey(privateKey));
            signature.update(requestBody.getBytes(UTF_8));
            return signature.sign();
        } catch (NoSuchAlgorithmException | SignatureException | InvalidKeyException e) {
            throw new CouldNotCreateSignatureException(e);
        }
    }

    default void verifySignature(String requestBody, byte[] signedBytes, String publicKey) {
        try {
            var securityKeyCreationService = new SecurityKeyCreationService();
            var signature = Signature.getInstance(SIGNATURE_ALGORITHM);
            signature.initVerify(securityKeyCreationService.createPublicKey(publicKey));
            signature.update(requestBody.getBytes(UTF_8));
            if (!signature.verify(signedBytes)) {
                throw new InvalidSignatureException();
            }
        } catch (NoSuchAlgorithmException | SignatureException | InvalidKeyException e) {
            throw new CouldNotVerifySignatureException(e);
        }
    }

    default byte[] decodeHex(String encodedSignature) {
        try {
            return Hex.decodeHex(encodedSignature.toCharArray());
        } catch (DecoderException e) {
            throw new CouldNotDecodeHexException(e);
        }
    }
}
