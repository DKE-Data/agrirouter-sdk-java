package com.dke.data.agrirouter.impl.common.ssl;

import com.dke.data.agrirouter.api.exception.CouldNotCreateDynamicKeyStoreException;
import com.dke.data.agrirouter.api.service.HasLogger;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.*;
import javax.crypto.EncryptedPrivateKeyInfo;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.net.SocketFactory;
import javax.net.ssl.*;

public class KeyStoreCreationService implements HasLogger {

  public static final String TEMPORARY_KEY_PASSWORD = "changeit";

  private static final String BEGIN_DELIMITER_PRIVATE_KEY = "-----BEGIN ENCRYPTED PRIVATE KEY-----";
  private static final String END_DELIMITER_PRIVATE_KEY = "-----END ENCRYPTED PRIVATE KEY-----";

  private static final String BEGIN_DELIMITER_CERTIFICATE = "-----BEGIN CERTIFICATE-----";
  private static final String END_DELIMITER_CERTIFICATE = "-----END CERTIFICATE-----";

  private static final String DEFAULT_PASSWORD = "changeit";

  public SocketFactory getSocketFactory(
      List<String> rootCertificates, String certificate, String password) throws Exception {
    this.logMethodBegin(rootCertificates, certificate, password);

    this.getNativeLogger().trace("Create trust managers.");
    TrustManager[] trustManagers = this.createTrustManagers(rootCertificates);

    this.getNativeLogger().trace("Init key manager factory.");
    KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
    kmf.init(this.createAndReturnKeystoreFromPEM(certificate, password), getDefaultPassword());

    this.getNativeLogger().trace("Generate SSL context.");
    SSLContext context = SSLContext.getInstance("TLSv1.2");

    this.getNativeLogger().trace("Initi SSL context.");
    context.init(kmf.getKeyManagers(), trustManagers, null);

    SSLSocketFactory socketFactory = context.getSocketFactory();
    this.logMethodEnd(socketFactory);
    return socketFactory;
  }

  public KeyStore createAndReturnKeystoreFromP12(String certificate, String password) {
    this.logMethodBegin(certificate, password);

    KeyStore keyStore;
    try {
      this.getNativeLogger().trace("Create PKCS12 instance for keystore.");
      keyStore = KeyStore.getInstance("PKCS12");

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
            X509Certificate cert =
                createCertificate(
                    extractFromOriginal(
                        certificate, BEGIN_DELIMITER_CERTIFICATE, END_DELIMITER_CERTIFICATE));

            this.getNativeLogger().trace("Create default keystore type.");
            KeyStore caKs = KeyStore.getInstance(KeyStore.getDefaultType());
            caKs.load(null, null);
            caKs.setCertificateEntry("ca-certificate", cert);

            this.getNativeLogger().trace("Create trust manager factory.");
            TrustManagerFactory tmf =
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
      X509Certificate cert =
          createCertificate(
              extractFromOriginal(
                  certificateAndPrivateKey,
                  BEGIN_DELIMITER_CERTIFICATE,
                  END_DELIMITER_CERTIFICATE));

      this.getNativeLogger().trace("Create private key.");
      PrivateKey key =
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
    KeyStore keystore = createKeyStore(cert, key);

    this.getNativeLogger().trace("Create random keystore name.");
    String tmpKeystoreName = UUID.randomUUID().toString();

    this.getNativeLogger().trace("Store keystore within temporary folder.");
    keystore.store(
        new FileOutputStream("./target/test-classes/" + tmpKeystoreName + ".jks"),
        TEMPORARY_KEY_PASSWORD.toCharArray());

    this.logMethodEnd(tmpKeystoreName);
    return tmpKeystoreName;
  }

  private String extractFromOriginal(String original, String beginDelimiter, String endDelimiter) {
    this.logMethodBegin(original, beginDelimiter, endDelimiter);

    this.getNativeLogger().trace("Split by delimiter.");
    String[] tokens = original.split(beginDelimiter);

    this.getNativeLogger().trace("Fetch second token as certificate from delimiter.");
    tokens = tokens[1].split(endDelimiter);

    this.getNativeLogger().trace("Replace all line breaks.");
    String certificate = tokens[0].replaceAll("\\s", "");

    this.logMethodEnd(certificate);
    return certificate;
  }

  PrivateKey createPrivateKey(String privateKey, String password) throws Exception {
    this.logMethodBegin(privateKey, password);

    this.getNativeLogger().trace("Create PBE key spec.");
    PBEKeySpec pbeSpec = new PBEKeySpec(password.toCharArray());

    this.getNativeLogger().trace("Create PK info using the private key.");
    EncryptedPrivateKeyInfo pkinfo =
        new EncryptedPrivateKeyInfo(Base64.getDecoder().decode(privateKey));

    this.getNativeLogger().trace("Create secret factory for PK info.");
    SecretKeyFactory skf = SecretKeyFactory.getInstance(pkinfo.getAlgName());

    this.getNativeLogger().trace("Generate secret.");
    Key secret = skf.generateSecret(pbeSpec);

    this.getNativeLogger().trace("Generate encoded key spec.");
    PKCS8EncodedKeySpec keySpec = pkinfo.getKeySpec(secret);

    this.getNativeLogger().trace("Create RSA key factory.");
    KeyFactory kf = KeyFactory.getInstance("RSA");

    PrivateKey privateKeyWithSpec = kf.generatePrivate(keySpec);
    this.logMethodEnd(privateKey);
    return privateKeyWithSpec;
  }

  X509Certificate createCertificate(String x509Certificate) throws Exception {
    this.logMethodBegin(x509Certificate);

    this.getNativeLogger().trace("Decode certificate.");
    byte[] certBytes = Base64.getDecoder().decode(x509Certificate);

    this.getNativeLogger().trace("Create 'X.509' certification factory.");
    CertificateFactory factory = CertificateFactory.getInstance("X.509");

    X509Certificate certificate =
        (X509Certificate) factory.generateCertificate(new ByteArrayInputStream(certBytes));
    this.logMethodEnd(certificate);
    return certificate;
  }

  private KeyStore createKeyStore(X509Certificate x509Certificate, PrivateKey key)
      throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException {
    this.logMethodBegin(x509Certificate, key);

    this.getNativeLogger().trace("Create JKS keystore.");
    KeyStore keystore = KeyStore.getInstance("JKS");
    keystore.load(null);
    keystore.setCertificateEntry("cert-alias", x509Certificate);

    this.getNativeLogger().trace("Add certificate to the key store.");
    keystore.setKeyEntry(
        "key-alias", key, getDefaultPassword(), new Certificate[] {x509Certificate});

    this.logMethodEnd(keystore);
    return keystore;
  }

  private static char[] getDefaultPassword() {
    return DEFAULT_PASSWORD.toCharArray();
  }
}
