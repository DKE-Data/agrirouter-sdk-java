package com.dke.data.agrirouter.test;

import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.NotImplementedException;

/** Abstract implementation for all integration tests. */
public abstract class AbstractIntegrationTest {

  /**
   * Since there are multiple problems with the stability of the QA env we define dedicated
   * constants for message fetching.
   */
  protected int MAX_TRIES_BEFORE_FAILURE = 10;

  protected long DEFAULT_INTERVAL = 5000;

  /**
   * Wait for the AR to process the messages. Since the QA has some stability problems, we will wait
   * up to 30 seconds until the AR has processed the messages.
   *
   * @throws InterruptedException -
   */
  protected void waitForTheAgrirouterToProcessSingleMessage() throws InterruptedException {
    Thread.sleep(TimeUnit.SECONDS.toMillis(30));
  }

  /**
   * Wait for the AR to process the messages. Since the QA has some stability problems, we will wait
   * up to one minute until the AR has processed the messages.
   *
   * @throws InterruptedException -
   */
  protected void waitForTheAgrirouterToProcessMultipleMessages() throws InterruptedException {
    Thread.sleep(TimeUnit.SECONDS.toMillis(60));
  }

  /** Communication unit for integration testing. */
  protected Application communicationUnit = new Application() {
      @Override
      public String getApplicationId() {
          return "434989e2-b4be-4cfd-8e40-f5b89d83458d";
      }

      @Override
      public String getCertificationVersionId() {
          return "f491d487-f913-4732-8be4-c2eacff21816";
      }
  };

  /** Farming software for integration testing. */
  protected Application farmingSoftware =
      new Application() {

        @Override
        public String getApplicationId() {
          return "905152eb-c526-47a3-b871-aa46d065bb4c";
        }

        @Override
        public String getCertificationVersionId() {
          return "c8ee0fc3-056c-4d81-8eba-fb4f8208c827";
        }

        @Override
        public String getPrivateKey() {
          return "-----BEGIN PRIVATE KEY-----\n"
              + "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDCiSNJmpAbrqF5\n"
              + "wOe4kPtavH50OxfjevZjgFhoG/lO+nkJz366655pjKqkwmlxF3GhoXMppAR2K+Dh\n"
              + "x3g46dJGA/dG5Ju5TpLnPn3AskHJMcagGE+0Lb29pXUX7ZAdO0MrqIVSh9UZ3j2v\n"
              + "+MlF4GOwUkOLPu8IgtKK13vlTu3qYKmFzSss9M3r/t7hZemGyGUq/GVZ8wvYWFTc\n"
              + "SdBhOZBzGDpQmRKiL5wcF1HyZfmT8mwbPITzf9Pd+9cDvQT74Ohmu920wwQavS+n\n"
              + "hPaOFEW9io9gIsKY3y81Qt4OR+xQ20BdmsRscWwWaSoQpjol0+f3OnTFmLMGvjuw\n"
              + "y8keLp+zAgMBAAECggEAGm87MdHEelaBD8PkWQKufTZ28oFjLiCz4XJ70O3YM2C6\n"
              + "2NfuzyScWphoSco3PMxkPwPq28IzbwFoZhXOYuF3FteMHwCKnqQsNysZOHmgkd/n\n"
              + "LzMOhZvfmCknH7DC8A3xAzo7n6rgjMrT1Bk49HtY4IlVL1NGdQAd7wr4BYFzt/Dc\n"
              + "8qXnqXWqWcvgktDQBKEPXc8XAQom6fWFcnx+CALUIx3c4QXaCgdG/EuWeaqqM14j\n"
              + "2tM1PwVh3rxiDiwMJSDEQVSxkJtM9M+EEC6oCvBPE4txjaHBjQqsxZJ9po5Va00+\n"
              + "Wm6DuPEfkKTxX1J0a/le8SA0JZ+Tlz/VXOySaeMKYQKBgQDpUoPOfpx/FfOIr4Tk\n"
              + "311zqcPfY+XiCwq56CTfVYERF8QSDQAMuuW3K53SFLTwjgUEq7BwFshhhcTdRxsh\n"
              + "QtKAHg4NpownmGnmQCG681eFTB4IiiGNMlEt6hXh3MQK4QfjDFL/0Y1tXStsM7xB\n"
              + "ouwPOZweWmlv7u7kVEDx4kHEUwKBgQDVcYp2pI/SjXtsFF/LRy/0xW/dgRQMortV\n"
              + "IPYkYvSO/w2dMV2wX1Ly78wFs4uj4ODDfyeVG2xDjn6svQR67FtHPLyBS+SwNTbY\n"
              + "jfzRV1EydHweZgdw9/1yA9zvSPVMvieSgeiR++2YmEEtJYSEoxuBkVSnLAXKFy3/\n"
              + "4RCHrhBLIQKBgB6im/XX//pbyn8u9JcMkPun1bUWK8/zPTRNu9GrK2gwI6lvFYuW\n"
              + "WqUjT/SOjXdsXlJPrLn689KCOugG9xP17yetSpEWRh7Iz1bRItymKN6ysNrUMDWW\n"
              + "3rvVmYvvbboz89InAxrdy+EJM7NgU56WosIZAVum6WMuDyXhvilEWPhLAoGALwnd\n"
              + "tPXhSEqr147J6befhvb4Bz3KGFrIpCMme7BfKyBkdK8LcbIgSq+0K9F8xbnqbssY\n"
              + "AxFPE7hUjGb/lMN//jwRYwFBvd+MXb8050GyAEeRjvV3UFsmvLjDOOzAOpBxkiUV\n"
              + "Bw8ZNpbfTj7FbKGxjyVjHZBjGj1vgsOr6+rdZmECgYEA5gBLtqvsebobo86J4LL7\n"
              + "6VaXtgbgShm8GYMfTDPxkmAv4a89Lpec2UQ6EaVt7xHSdk4hMidfZHGambOtd+Zh\n"
              + "DTWnVSzxt91ZSEcDc3gTxK3hBltTga+9Y63S+/6vVykWKRtAvqkiEEgLNnNXOFNX\n"
              + "lE8ylEsO1whZyzKceV8uaqA=\n"
              + "-----END PRIVATE KEY-----\n";
        }

        @Override
        public String getPublicKey() {
          return "-----BEGIN PUBLIC KEY-----\n"
              + "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwokjSZqQG66hecDnuJD7\n"
              + "Wrx+dDsX43r2Y4BYaBv5Tvp5Cc9+uuueaYyqpMJpcRdxoaFzKaQEdivg4cd4OOnS\n"
              + "RgP3RuSbuU6S5z59wLJByTHGoBhPtC29vaV1F+2QHTtDK6iFUofVGd49r/jJReBj\n"
              + "sFJDiz7vCILSitd75U7t6mCphc0rLPTN6/7e4WXphshlKvxlWfML2FhU3EnQYTmQ\n"
              + "cxg6UJkSoi+cHBdR8mX5k/JsGzyE83/T3fvXA70E++DoZrvdtMMEGr0vp4T2jhRF\n"
              + "vYqPYCLCmN8vNULeDkfsUNtAXZrEbHFsFmkqEKY6JdPn9zp0xZizBr47sMvJHi6f\n"
              + "swIDAQAB\n"
              + "-----END PUBLIC KEY-----\n";
        }
      };

  /** Farming software for integration testing. */
  protected Application telemetryPlatform =
      new Application() {

        @Override
        public String getApplicationId() {
          return "3c3559c9-7062-4628-a4f7-c9f5aa07265f";
        }

        @Override
        public String getCertificationVersionId() {
          return "5e488554-d6bb-400c-9632-b3b4b35b07ca";
        }

        @Override
        public String getPrivateKey() {
          return "-----BEGIN PRIVATE KEY-----\n"
              + "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCkAWrcN0FmbWZ+\n"
              + "24qE6maNlf46EjH/BOI5f79wFRJAeG6RXrl8KwWdlfeZOlEYRyq+o4AVuP7Mce6R\n"
              + "x5PR/AEtn1K4Twr5h1+uG+O5/D2jJ0O/+nFnNcX/WgJfEbriDK9ewV8mEnFgG3n5\n"
              + "y6P9RCcu0zMFV9PrKTop419krg2Ij0/to5LLU0DaQXmgvSC7Crx0k9dIhDeZ4jc3\n"
              + "rv4SWzOcZT5vPFtA/5G66ZeeTVX+3iJbasWOdatMc7/cGYxUlnzeNSoCAqmyuHzO\n"
              + "PKHWcjyVwPXWG64TSzkb+QypynUIZaAy+P8uawD0BivsmFSf0k4pGl8wSHhE2pN8\n"
              + "OIIyud/lAgMBAAECgf93UZCtJoYuPumS4aaljOOPntCW7yXwo1zy+D4PDUV6IiRP\n"
              + "HttTuvka7UB1+jeFskEm8Uz+gNjfZQRsiwbAftdcmc1Uyizx3ct+oEvNw/YT/T0N\n"
              + "LufSbwzd+l4/TGbqjtAH4CeZS1Gw2kyjr8RfPnZDWuSDvqIvNI9cak+8r+SotHEL\n"
              + "i1yAZTDvZAqLhbDLbW9Ephxd5QkKfwegybSwxJJcOw+KLPsq0Ibb2YGbkPTL1P6o\n"
              + "Ul3/tiyzEMRc2ufyDJgYCAUmvVBjkbHT7hQNkTL8EPAEXfL+vhte8A98aFPnWMhS\n"
              + "yiJo3T643fjZ8fzPcv8GWmT+z6IxtlXguC/OmQECgYEAzUbQF5YqJ61n6sQiJ0VQ\n"
              + "ZMDDVHzf/oNUCPee4tCVS0ShQ2JURUIbss+lUWbKDfFTXW9mte4VRk+WUh2gQqm6\n"
              + "49IdIpRGx6m6/ye2g6EmXRzeSN9GJu/wltS2uQ+8fmOkWiBUFg8bkLWTTxAY1w0n\n"
              + "31AYcE8wwLb2FXt6Q2u4OQECgYEAzIfrUejd7GS/eaUx2GFKsXaI3B3CmLQ8TQFu\n"
              + "JOh3i2NgcDK5Uwhy5TWozEn4BpwX8UOMciZIGjMr0i5TzpSVca1sv1wyIyWr7+AU\n"
              + "2dmxPTXbkQQpEy7GDE1iCMvsi1lC6F+EUwdCKw6H5oa0oU1XMr/JWJSGh8iFRH5v\n"
              + "H3ic4uUCgYEAycZYV0boqtWddrtRou5UBqUfmxWgC4nFeYcE64gBp9mO9jBaCzXf\n"
              + "ChVnN6tk4u2adxZAjIW47cLfTFoIpF438SYgM1QyeqIQyCueEKa/kfkFWfX1++IP\n"
              + "yALQlPmt20JQU7LBVGmHO+fvI2D8Oa6ZybzuOL7ueg+dtiIIXOsuSwECgYEAjqaZ\n"
              + "1PYymmamOWSjQESTQPTofYVwLOtAR4Eg3jMY9anpIDfwk94HrJ/hTCKnD40dEdPI\n"
              + "B/F1Rni8LorLPwDYvoFzoH/gJC1pHxPN1yeC+6stqZYaQ9vSm8/4+SOppoMqLEI6\n"
              + "W2JrnvYyiPEY2IvFroqfFt34eom7kUsRnASWf5UCgYEAxEMOQ0/+hz1APqCGVo3t\n"
              + "uePUIdsokCusRpWsxUdwHBGqfDjULqVmDXIJuJekV4YUxzyzktRzVms0pgFbr47L\n"
              + "f8IeyJnYFhPn1hgthGlgV7JvFq2TU+EgZYVpVGL6jrw8t78Y4IresceIkz4q/bGV\n"
              + "j35jwp22eLOc0FdPo0KgO4s=\n"
              + "-----END PRIVATE KEY-----\n";
        }

        @Override
        public String getPublicKey() {
          return "-----BEGIN PUBLIC KEY-----\n"
              + "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEApAFq3DdBZm1mftuKhOpm\n"
              + "jZX+OhIx/wTiOX+/cBUSQHhukV65fCsFnZX3mTpRGEcqvqOAFbj+zHHukceT0fwB\n"
              + "LZ9SuE8K+Ydfrhvjufw9oydDv/pxZzXF/1oCXxG64gyvXsFfJhJxYBt5+cuj/UQn\n"
              + "LtMzBVfT6yk6KeNfZK4NiI9P7aOSy1NA2kF5oL0guwq8dJPXSIQ3meI3N67+Elsz\n"
              + "nGU+bzxbQP+RuumXnk1V/t4iW2rFjnWrTHO/3BmMVJZ83jUqAgKpsrh8zjyh1nI8\n"
              + "lcD11huuE0s5G/kMqcp1CGWgMvj/LmsA9AYr7JhUn9JOKRpfMEh4RNqTfDiCMrnf\n"
              + "5QIDAQAB\n"
              + "-----END PUBLIC KEY-----\n";
        }
      };

  /** Representation of an application. */
  public abstract static class Application {

    /**
     * Application for all the test cases.
     *
     * @return -
     */
    public abstract String getApplicationId();

    /**
     * Version for all the test cases.
     *
     * @return -
     */
    public abstract String getCertificationVersionId();

    /**
     * Private key for the application.
     *
     * @return -
     */
    public String getPrivateKey() {
      throw new NotImplementedException("This application does not support private keys.");
    }

    /**
     * Public key for the application.
     *
     * @return -
     */
    public String getPublicKey() {
      throw new NotImplementedException("This application does not support private keys.");
    }
  }
}
