package com.dke.data.agrirouter.impl.common.ssl;

import com.dke.data.agrirouter.api.exception.CouldNotCreateDynamicKeyStoreException;
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
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

public class KeyStoreCreationUtils {

  public static final String TEMPORARY_KEY_PASSWORD = "changeit";

  private static final String BEGIN_DELIMITER_PRIVATE_KEY = "-----BEGIN ENCRYPTED PRIVATE KEY-----";
  private static final String END_DELIMITER_PRIVATE_KEY = "-----END ENCRYPTED PRIVATE KEY-----";

  private static final String BEGIN_DELIMITER_CERTIFICATE = "-----BEGIN CERTIFICATE-----";
  private static final String END_DELIMITER_CERTIFICATE = "-----END CERTIFICATE-----";
  private static final String DEFAULT_PASSWORD = "changeit";

  public static SocketFactory getSocketFactory(
      List<String> rootCertificates, String certificate, String password) throws Exception {
    TrustManager[] trustManagers = KeyStoreCreationUtils.createTrustManagers(rootCertificates);
    KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
    kmf.init(
        KeyStoreCreationUtils.createAndReturnKeystoreFromPEM(certificate, password),
        getDefaultPassword());
    SSLContext context = SSLContext.getInstance("TLSv1.2");
    context.init(kmf.getKeyManagers(), trustManagers, null);
    return context.getSocketFactory();
  }

  public static KeyStore createAndReturnKeystoreFromP12(String certificate, String password) {
    try {
      KeyStore keyStore = KeyStore.getInstance("PKCS12");
      InputStream sslInputStream =
          new ByteArrayInputStream(Base64.getDecoder().decode(certificate.getBytes()));
      keyStore.load(sslInputStream, password.toCharArray());
      return keyStore;
    } catch (Exception e) {
      throw new CouldNotCreateDynamicKeyStoreException(e);
    }
  }

  private static TrustManager[] createTrustManagers(List<String> certificates) {
    List<TrustManager> trustManagers = new ArrayList<>();
    certificates.forEach(
        certificate -> {
          try {
            X509Certificate cert =
                createCertificate(
                    extractFromOriginal(
                        certificate, BEGIN_DELIMITER_CERTIFICATE, END_DELIMITER_CERTIFICATE));
            KeyStore caKs = KeyStore.getInstance(KeyStore.getDefaultType());
            caKs.load(null, null);
            caKs.setCertificateEntry("ca-certificate", cert);
            TrustManagerFactory tmf =
                TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(caKs);
            trustManagers.addAll(Arrays.asList(tmf.getTrustManagers()));
          } catch (Exception e) {
            throw new CouldNotCreateDynamicKeyStoreException(e);
          }
        });
    return trustManagers.toArray(new TrustManager[0]);
  }

  public static KeyStore createAndReturnKeystoreFromPEM(
      String certificateAndPrivateKey, String password) {
    try {
      X509Certificate cert =
          createCertificate(
              extractFromOriginal(
                  certificateAndPrivateKey,
                  BEGIN_DELIMITER_CERTIFICATE,
                  END_DELIMITER_CERTIFICATE));
      PrivateKey key =
          createPrivateKey(
              extractFromOriginal(
                  certificateAndPrivateKey, BEGIN_DELIMITER_PRIVATE_KEY, END_DELIMITER_PRIVATE_KEY),
              password);
      return createKeyStore(cert, key);
    } catch (Exception e) {
      throw new CouldNotCreateDynamicKeyStoreException(e);
    }
  }

  static String createKeyStoreInClasspath(X509Certificate cert, PrivateKey key)
      throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException {
    KeyStore keystore = createKeyStore(cert, key);
    String tmpKeystoreName = UUID.randomUUID().toString();
    keystore.store(
        new FileOutputStream("./target/test-classes/" + tmpKeystoreName + ".jks"),
        TEMPORARY_KEY_PASSWORD.toCharArray());
    return tmpKeystoreName;
  }

  private static String extractFromOriginal(
      String original, String beginDelimiter, String endDelimiter) {
    String[] tokens = original.split(beginDelimiter);
    tokens = tokens[1].split(endDelimiter);
    return tokens[0].replaceAll("\\s", "");
  }

  static PrivateKey createPrivateKey(String privateKey, String password) throws Exception {
    PBEKeySpec pbeSpec = new PBEKeySpec(password.toCharArray());
    EncryptedPrivateKeyInfo pkinfo =
        new EncryptedPrivateKeyInfo(Base64.getDecoder().decode(privateKey));
    SecretKeyFactory skf = SecretKeyFactory.getInstance(pkinfo.getAlgName());
    Key secret = skf.generateSecret(pbeSpec);
    PKCS8EncodedKeySpec keySpec = pkinfo.getKeySpec(secret);
    KeyFactory kf = KeyFactory.getInstance("RSA");
    return kf.generatePrivate(keySpec);
  }

  static X509Certificate createCertificate(String x509Certificate) throws Exception {
    byte[] certBytes = Base64.getDecoder().decode(x509Certificate);
    CertificateFactory factory = CertificateFactory.getInstance("X.509");
    return (X509Certificate) factory.generateCertificate(new ByteArrayInputStream(certBytes));
  }

  private static KeyStore createKeyStore(X509Certificate x509Certificate, PrivateKey key)
      throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException {
    KeyStore keystore = KeyStore.getInstance("JKS");
    keystore.load(null);
    keystore.setCertificateEntry("cert-alias", x509Certificate);
    keystore.setKeyEntry(
        "key-alias", key, getDefaultPassword(), new Certificate[] {x509Certificate});
    return keystore;
  }

  public static char[] getDefaultPassword() {
    return DEFAULT_PASSWORD.toCharArray();
  }
}
