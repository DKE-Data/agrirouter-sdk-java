package com.dke.data.agrirouter.impl.common.signing;

import com.dke.data.agrirouter.api.exception.CouldNotCreatePrivateKeyException;
import com.dke.data.agrirouter.api.exception.CouldNotCreatePublicKeyException;
import com.dke.data.agrirouter.api.service.HasLogger;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class SecurityKeyCreationService implements HasLogger {

    public PrivateKey createPrivateKey(String privateKey) {
        this.logMethodBegin(privateKey);

        PrivateKey result;
        try {
            this.getNativeLogger().trace("Replacing comments within file.");
            var pkcs8Pem = privateKey.replace("-----BEGIN PRIVATE KEY-----", "");
            pkcs8Pem = pkcs8Pem.replace("-----END PRIVATE KEY-----", "");
            pkcs8Pem = pkcs8Pem.replaceAll("\\s+", "");

            this.getNativeLogger().trace("Decode base 64 values.");
            var pkcs8EncodedBytes = Base64.getDecoder().decode(pkcs8Pem);

            this.getNativeLogger().trace("Generate private key.");
            var keySpec = new PKCS8EncodedKeySpec(pkcs8EncodedBytes);
            var keyFactory = KeyFactory.getInstance("RSA");
            result = keyFactory.generatePrivate(keySpec);
        } catch (IllegalArgumentException | InvalidKeySpecException | NoSuchAlgorithmException e) {
            throw new CouldNotCreatePrivateKeyException(e);
        }

        this.logMethodEnd(result);
        return result;
    }

    public PublicKey createPublicKey(String publicKey) {
        this.logMethodBegin(publicKey);

        PublicKey result;
        try {
            this.getNativeLogger().trace("Replacing comments within file.");
            var pkcs8Pem = publicKey.replace("-----BEGIN PUBLIC KEY-----", "");
            pkcs8Pem = pkcs8Pem.replace("-----END PUBLIC KEY-----", "");
            pkcs8Pem = pkcs8Pem.replaceAll("\\s+", "");

            this.getNativeLogger().trace("Decode base 64 values.");
            var pkcs8EncodedBytes = Base64.getDecoder().decode(pkcs8Pem);

            this.getNativeLogger().trace("Generate public key.");
            var keySpec = new X509EncodedKeySpec(pkcs8EncodedBytes);
            var keyFactory = KeyFactory.getInstance("RSA");
            result = keyFactory.generatePublic(keySpec);
        } catch (IllegalArgumentException | InvalidKeySpecException | NoSuchAlgorithmException e) {
            throw new CouldNotCreatePublicKeyException(e);
        }

        this.logMethodEnd(result);
        return result;
    }
}
