package com.dke.data.agrirouter.impl.common.ssl;

import com.dke.data.agrirouter.api.env.Constants;
import com.dke.data.agrirouter.api.exception.CouldNotCreateDynamicKeyStoreException;
import com.dke.data.agrirouter.api.service.HasLogger;

import javax.crypto.EncryptedPrivateKeyInfo;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.net.SocketFactory;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.*;

public class KeyStoreCreationService implements HasLogger {

    private static final String BEGIN_DELIMITER_PRIVATE_KEY = "-----BEGIN ENCRYPTED PRIVATE KEY-----";
    private static final String END_DELIMITER_PRIVATE_KEY = "-----END ENCRYPTED PRIVATE KEY-----";

    private static final String BEGIN_DELIMITER_CERTIFICATE = "-----BEGIN CERTIFICATE-----";
    private static final String END_DELIMITER_CERTIFICATE = "-----END CERTIFICATE-----";

    public SocketFactory getSocketFactory(
            List<String> rootCertificates, String certificate, String password) throws Exception {
        this.logMethodBegin(rootCertificates, certificate, password);

        this.getNativeLogger().trace("Create trust managers.");
        var trustManagers = this.createTrustManagers(rootCertificates);

        this.getNativeLogger().trace("Init key manager factory.");
        var kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(this.createAndReturnKeystoreFromPEM(certificate, password), getDefaultPassword());

        this.getNativeLogger().trace("Generate SSL context.");
        var context = SSLContext.getInstance("TLSv1.2");

        this.getNativeLogger().trace("Init SSL context.");
        context.init(kmf.getKeyManagers(), trustManagers, null);

        var socketFactory = context.getSocketFactory();
        this.logMethodEnd(socketFactory);
        return socketFactory;
    }

    public KeyStore createAndReturnKeystoreFromP12(String certificate, String password) {
        this.logMethodBegin(certificate, password);

        KeyStore keyStore;
        try {
            this.getNativeLogger().trace("Create PKCS12 instance for keystore.");
            keyStore = KeyStore.getInstance(KeyStore.getDefaultType());

            this.getNativeLogger().trace("Create input stream for certificate.");
            InputStream sslInputStream =
                    new ByteArrayInputStream(Base64.getDecoder().decode(certificate.getBytes()));

            this.getNativeLogger().trace("Load input stream into keystore.");
            keyStore.load(sslInputStream, password.toCharArray());

        } catch (Exception e) {
            throw new CouldNotCreateDynamicKeyStoreException(e);
        }

        this.logMethodEnd(keyStore);
        return keyStore;
    }

    public TrustManager[] createTrustManagers(List<String> certificates) {
        this.logMethodBegin(certificates);

        List<TrustManager> trustManagers = new ArrayList<>();
        certificates.forEach(
                certificate -> {
                    try {
                        this.getNativeLogger().trace("Create certificate for '{}'.", certificate);
                        var cert =
                                createCertificate(
                                        extractFromOriginal(
                                                certificate, BEGIN_DELIMITER_CERTIFICATE, END_DELIMITER_CERTIFICATE));

                        this.getNativeLogger().trace("Create default keystore type.");
                        var caKs = KeyStore.getInstance(KeyStore.getDefaultType());
                        caKs.load(null, null);
                        caKs.setCertificateEntry("ca-certificate", cert);

                        this.getNativeLogger().trace("Create trust manager factory.");
                        var tmf =
                                TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());

                        this.getNativeLogger().trace("Init trust manager factory.");
                        tmf.init(caKs);

                        this.getNativeLogger().trace("Add all trust managers from factory.");
                        trustManagers.addAll(Arrays.asList(tmf.getTrustManagers()));
                    } catch (Exception e) {
                        throw new CouldNotCreateDynamicKeyStoreException(e);
                    }
                });

        this.logMethodEnd(trustManagers);
        return trustManagers.toArray(new TrustManager[0]);
    }

    public KeyStore createAndReturnKeystoreFromPEM(String certificateAndPrivateKey, String password) {
        this.logMethodBegin(certificateAndPrivateKey, password);

        KeyStore keyStore;
        try {
            this.getNativeLogger().trace("Create certificate.");
            var cert =
                    createCertificate(
                            extractFromOriginal(
                                    certificateAndPrivateKey,
                                    BEGIN_DELIMITER_CERTIFICATE,
                                    END_DELIMITER_CERTIFICATE));

            this.getNativeLogger().trace("Create private key.");
            var key =
                    createPrivateKey(
                            extractFromOriginal(
                                    certificateAndPrivateKey, BEGIN_DELIMITER_PRIVATE_KEY, END_DELIMITER_PRIVATE_KEY),
                            password);

            this.getNativeLogger().trace("Create key store.");
            keyStore = createKeyStore(cert, key);
        } catch (Exception e) {
            throw new CouldNotCreateDynamicKeyStoreException(e);
        }

        this.logMethodEnd(keyStore);
        return keyStore;
    }

    String createKeyStoreInClasspath(X509Certificate cert, PrivateKey key)
            throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException {
        this.logMethodBegin(cert, key);

        this.getNativeLogger().trace("Create keystore.");
        var keystore = createKeyStore(cert, key);

        this.getNativeLogger().trace("Create random keystore name.");
        var tmpKeystoreName = UUID.randomUUID().toString();

        this.getNativeLogger().trace("Store keystore within temporary folder.");
        keystore.store(
                Files.newOutputStream(Paths.get("./target/test-classes/" + tmpKeystoreName + ".jks")),
                Constants.DEFAULT_PASSWORD.toCharArray());

        this.logMethodEnd(tmpKeystoreName);
        return tmpKeystoreName;
    }

    private String extractFromOriginal(String original, String beginDelimiter, String endDelimiter) {
        this.logMethodBegin(original, beginDelimiter, endDelimiter);

        this.getNativeLogger().trace("Split by delimiter.");
        var tokens = original.split(beginDelimiter);

        this.getNativeLogger().trace("Fetch second token as certificate from delimiter.");
        tokens = tokens[1].split(endDelimiter);

        this.getNativeLogger().trace("Replace all line breaks.");
        var certificate = tokens[0].replaceAll("\\s", "");

        this.logMethodEnd(certificate);
        return certificate;
    }

    PrivateKey createPrivateKey(String privateKey, String password) throws Exception {
        this.logMethodBegin(privateKey, password);

        this.getNativeLogger().trace("Create PBE key spec.");
        var pbeSpec = new PBEKeySpec(password.toCharArray());

        this.getNativeLogger().trace("Create PK info using the private key.");
        var pkinfo =
                new EncryptedPrivateKeyInfo(Base64.getDecoder().decode(privateKey));

        this.getNativeLogger().trace("Create secret factory for PK info.");
        var skf = SecretKeyFactory.getInstance(pkinfo.getAlgName());

        this.getNativeLogger().trace("Generate secret.");
        Key secret = skf.generateSecret(pbeSpec);

        this.getNativeLogger().trace("Generate encoded key spec.");
        var keySpec = pkinfo.getKeySpec(secret);

        this.getNativeLogger().trace("Create RSA key factory.");
        var kf = KeyFactory.getInstance("RSA");

        var privateKeyWithSpec = kf.generatePrivate(keySpec);
        this.logMethodEnd(privateKey);
        return privateKeyWithSpec;
    }

    X509Certificate createCertificate(String x509Certificate) throws Exception {
        this.logMethodBegin(x509Certificate);

        this.getNativeLogger().trace("Decode certificate.");
        var certBytes = Base64.getDecoder().decode(x509Certificate);

        this.getNativeLogger().trace("Create 'X.509' certification factory.");
        var factory = CertificateFactory.getInstance("X.509");

        var certificate =
                (X509Certificate) factory.generateCertificate(new ByteArrayInputStream(certBytes));
        this.logMethodEnd(certificate);
        return certificate;
    }

    private KeyStore createKeyStore(X509Certificate x509Certificate, PrivateKey key)
            throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException {
        this.logMethodBegin(x509Certificate, key);

        this.getNativeLogger().trace("Create JKS keystore.");
        var keystore = KeyStore.getInstance("JKS");
        keystore.load(null);
        keystore.setCertificateEntry(Constants.CERT_ALIAS, x509Certificate);

        this.getNativeLogger().trace("Add certificate to the key store.");
        keystore.setKeyEntry(
                Constants.KEY_ALIAS, key, getDefaultPassword(), new Certificate[]{x509Certificate});

        this.logMethodEnd(keystore);
        return keystore;
    }

    private static char[] getDefaultPassword() {
        return Constants.DEFAULT_PASSWORD.toCharArray();
    }
}
