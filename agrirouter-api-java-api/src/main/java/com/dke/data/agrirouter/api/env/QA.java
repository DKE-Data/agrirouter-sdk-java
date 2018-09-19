package com.dke.data.agrirouter.api.env;

import java.util.Arrays;
import java.util.List;

/** Abstraction of the QA environment, currently no overrides because the default is QA already. */
public abstract class QA implements Environment {

  private static final String ENV_BASE_URL = "https://agrirouter-qa.cfapps.eu1.hana.ondemand.com";
  private static final String API_PREFIX = "/api/v1.0";
  private static final String REGISTRATION_SERVICE_URL =
      "https://agrirouter-registration-service-hubqa-eu1.cfapps.eu1.hana.ondemand.com";

  private String rootCertificate1 =
      "-----BEGIN CERTIFICATE-----\n"
          + "MIIGPTCCBSWgAwIBAgIQD/7fN6LZo1Diwt48WHj2MDANBgkqhkiG9w0BAQsFADBN\n"
          + "MQswCQYDVQQGEwJVUzEVMBMGA1UEChMMRGlnaUNlcnQgSW5jMScwJQYDVQQDEx5E\n"
          + "aWdpQ2VydCBTSEEyIFNlY3VyZSBTZXJ2ZXIgQ0EwHhcNMTcxMjA3MDAwMDAwWhcN\n"
          + "MTgxMjA4MTIwMDAwWjB3MQswCQYDVQQGEwJERTERMA8GA1UEBxMIV2FsbGRvcmYx\n"
          + "DDAKBgNVBAoTA1NBUDEuMCwGA1UECxMlU0FQIENsb3VkIFBsYXRmb3JtIEludGVy\n"
          + "bmV0IG9mIFRoaW5nczEXMBUGA1UEAxMOZXUxLmNwLmlvdC5zYXAwggEiMA0GCSqG\n"
          + "SIb3DQEBAQUAA4IBDwAwggEKAoIBAQDh9naF1TeelRIkqILXA9dtbvryZl7fcT4l\n"
          + "2rHdYhWPDEi5y0JEDxJOAbJdMcg0CcQ0GkKtf62pYNFgfB853JLmRlhlDRgeKdz2\n"
          + "OBtFhqkqVeL8zlD8kjwZLxhEFJrUyvzKIs7g0YYpXWKYoPLIAtjccENyYwdKKTwZ\n"
          + "RFqVN1Dq/6amfOrQ0lZux0GOQzImwSfFVO0skXONVd4VwKjNZ9CkUdgKSdjzFTl3\n"
          + "zkF5/YWfImG0edWVjgt6iW+L8XdGm6Sn0p5evV2ZVURNYsvy1+t5KMB3rrYvNZtL\n"
          + "/+WiIU0oJwY0wlHu0kwYH3BJ0J9L3MlKFP21bTstn+NzWe8Hy+frAgMBAAGjggLt\n"
          + "MIIC6TAfBgNVHSMEGDAWgBQPgGEcgjFh1S8o541GOLQs4cbZ4jAdBgNVHQ4EFgQU\n"
          + "gcItVb8pHfO0vXuk066v1AoAxOIwKwYDVR0RBCQwIoIQKi5ldTEuY3AuaW90LnNh\n"
          + "cIIOZXUxLmNwLmlvdC5zYXAwDgYDVR0PAQH/BAQDAgWgMB0GA1UdJQQWMBQGCCsG\n"
          + "AQUFBwMBBggrBgEFBQcDAjBrBgNVHR8EZDBiMC+gLaArhilodHRwOi8vY3JsMy5k\n"
          + "aWdpY2VydC5jb20vc3NjYS1zaGEyLWc2LmNybDAvoC2gK4YpaHR0cDovL2NybDQu\n"
          + "ZGlnaWNlcnQuY29tL3NzY2Etc2hhMi1nNi5jcmwwTAYDVR0gBEUwQzA3BglghkgB\n"
          + "hv1sAQEwKjAoBggrBgEFBQcCARYcaHR0cHM6Ly93d3cuZGlnaWNlcnQuY29tL0NQ\n"
          + "UzAIBgZngQwBAgIwfAYIKwYBBQUHAQEEcDBuMCQGCCsGAQUFBzABhhhodHRwOi8v\n"
          + "b2NzcC5kaWdpY2VydC5jb20wRgYIKwYBBQUHMAKGOmh0dHA6Ly9jYWNlcnRzLmRp\n"
          + "Z2ljZXJ0LmNvbS9EaWdpQ2VydFNIQTJTZWN1cmVTZXJ2ZXJDQS5jcnQwCQYDVR0T\n"
          + "BAIwADCCAQUGCisGAQQB1nkCBAIEgfYEgfMA8QB3AKS5CZC0GFgUh7sTosxncAo8\n"
          + "NZgE+RvfuON3zQ7IDdwQAAABYC/kapYAAAQDAEgwRgIhALyRlv0KjKPG80qg/qcF\n"
          + "oPWvsehhs+X6hziSXD34w4rAAiEAiAp/H+VW4BAG3lRSyoB4t9rDDNqOaQWGSkH5\n"
          + "5/ti1nYAdgCHdb/nWXz4jEOZX73zbv9WjUdWNv9KtWDBtOr/XqCDDwAAAWAv5GtF\n"
          + "AAAEAwBHMEUCIQD3TfdBBEWG2f2zE154szS0l4T97HPvxa2GwJL47BKUdQIgWHKc\n"
          + "iPfklHbL5fkKM0TFCxuceB304vl6U5gW/KjIsBkwDQYJKoZIhvcNAQELBQADggEB\n"
          + "AB2N7ZDc3hx7Qdx324TtqiuDaeYxmYdcD7YV/3rsOgNm1XYPR7r3SdeAN+ZdyvZB\n"
          + "pAQGy51ma+FYrWXDu3QmtuwJAV7aLz4cnleFD7Fbq4ySuQdpwIlaO1qKwiMy5fb6\n"
          + "0Jp98UH/vSBJ1ZJv4ma5VlGBTRH9/Ux3mz55p4KGRtwSVAf6xj/PNvhJ08k4UDtU\n"
          + "6+FRhWlJ9sddU4Xzmy6qgqgNj4zR9pWOIESVH20k7mHRy8UaYcjqsQSCGGLznPkB\n"
          + "J42uw2gA//Nlssz4FUEm5pubtW3hnOFz/SVwitgGlPQgIx8mPI9ehRESG2x0k+Oq\n"
          + "xR60ZroCfBObSkMHidrKP3U=\n"
          + "-----END CERTIFICATE-----";

  private String rootCertificate2 =
      "-----BEGIN CERTIFICATE-----\n"
          + "MIIElDCCA3ygAwIBAgIQAf2j627KdciIQ4tyS8+8kTANBgkqhkiG9w0BAQsFADBh\n"
          + "MQswCQYDVQQGEwJVUzEVMBMGA1UEChMMRGlnaUNlcnQgSW5jMRkwFwYDVQQLExB3\n"
          + "d3cuZGlnaWNlcnQuY29tMSAwHgYDVQQDExdEaWdpQ2VydCBHbG9iYWwgUm9vdCBD\n"
          + "QTAeFw0xMzAzMDgxMjAwMDBaFw0yMzAzMDgxMjAwMDBaME0xCzAJBgNVBAYTAlVT\n"
          + "MRUwEwYDVQQKEwxEaWdpQ2VydCBJbmMxJzAlBgNVBAMTHkRpZ2lDZXJ0IFNIQTIg\n"
          + "U2VjdXJlIFNlcnZlciBDQTCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEB\n"
          + "ANyuWJBNwcQwFZA1W248ghX1LFy949v/cUP6ZCWA1O4Yok3wZtAKc24RmDYXZK83\n"
          + "nf36QYSvx6+M/hpzTc8zl5CilodTgyu5pnVILR1WN3vaMTIa16yrBvSqXUu3R0bd\n"
          + "KpPDkC55gIDvEwRqFDu1m5K+wgdlTvza/P96rtxcflUxDOg5B6TXvi/TC2rSsd9f\n"
          + "/ld0Uzs1gN2ujkSYs58O09rg1/RrKatEp0tYhG2SS4HD2nOLEpdIkARFdRrdNzGX\n"
          + "kujNVA075ME/OV4uuPNcfhCOhkEAjUVmR7ChZc6gqikJTvOX6+guqw9ypzAO+sf0\n"
          + "/RR3w6RbKFfCs/mC/bdFWJsCAwEAAaOCAVowggFWMBIGA1UdEwEB/wQIMAYBAf8C\n"
          + "AQAwDgYDVR0PAQH/BAQDAgGGMDQGCCsGAQUFBwEBBCgwJjAkBggrBgEFBQcwAYYY\n"
          + "aHR0cDovL29jc3AuZGlnaWNlcnQuY29tMHsGA1UdHwR0MHIwN6A1oDOGMWh0dHA6\n"
          + "Ly9jcmwzLmRpZ2ljZXJ0LmNvbS9EaWdpQ2VydEdsb2JhbFJvb3RDQS5jcmwwN6A1\n"
          + "oDOGMWh0dHA6Ly9jcmw0LmRpZ2ljZXJ0LmNvbS9EaWdpQ2VydEdsb2JhbFJvb3RD\n"
          + "QS5jcmwwPQYDVR0gBDYwNDAyBgRVHSAAMCowKAYIKwYBBQUHAgEWHGh0dHBzOi8v\n"
          + "d3d3LmRpZ2ljZXJ0LmNvbS9DUFMwHQYDVR0OBBYEFA+AYRyCMWHVLyjnjUY4tCzh\n"
          + "xtniMB8GA1UdIwQYMBaAFAPeUDVW0Uy7ZvCj4hsbw5eyPdFVMA0GCSqGSIb3DQEB\n"
          + "CwUAA4IBAQAjPt9L0jFCpbZ+QlwaRMxp0Wi0XUvgBCFsS+JtzLHgl4+mUwnNqipl\n"
          + "5TlPHoOlblyYoiQm5vuh7ZPHLgLGTUq/sELfeNqzqPlt/yGFUzZgTHbO7Djc1lGA\n"
          + "8MXW5dRNJ2Srm8c+cftIl7gzbckTB+6WohsYFfZcTEDts8Ls/3HB40f/1LkAtDdC\n"
          + "2iDJ6m6K7hQGrn2iWZiIqBtvLfTyyRRfJs8sjX7tN8Cp1Tm5gr8ZDOo0rwAhaPit\n"
          + "c+LJMto4JQtV05od8GiG7S5BNO98pVAdvzr508EIDObtHopYJeS4d60tbvVS3bR0\n"
          + "j6tJLp07kzQoH3jOlOrHvdPJbRzeXDLz\n"
          + "-----END CERTIFICATE-----";

  private String rootCertificate3 =
      "-----BEGIN CERTIFICATE-----\n"
          + "MIIDrzCCApegAwIBAgIQCDvgVpBCRrGhdWrJWZHHSjANBgkqhkiG9w0BAQUFADBh\n"
          + "MQswCQYDVQQGEwJVUzEVMBMGA1UEChMMRGlnaUNlcnQgSW5jMRkwFwYDVQQLExB3\n"
          + "d3cuZGlnaWNlcnQuY29tMSAwHgYDVQQDExdEaWdpQ2VydCBHbG9iYWwgUm9vdCBD\n"
          + "QTAeFw0wNjExMTAwMDAwMDBaFw0zMTExMTAwMDAwMDBaMGExCzAJBgNVBAYTAlVT\n"
          + "MRUwEwYDVQQKEwxEaWdpQ2VydCBJbmMxGTAXBgNVBAsTEHd3dy5kaWdpY2VydC5j\n"
          + "b20xIDAeBgNVBAMTF0RpZ2lDZXJ0IEdsb2JhbCBSb290IENBMIIBIjANBgkqhkiG\n"
          + "9w0BAQEFAAOCAQ8AMIIBCgKCAQEA4jvhEXLeqKTTo1eqUKKPC3eQyaKl7hLOllsB\n"
          + "CSDMAZOnTjC3U/dDxGkAV53ijSLdhwZAAIEJzs4bg7/fzTtxRuLWZscFs3YnFo97\n"
          + "nh6Vfe63SKMI2tavegw5BmV/Sl0fvBf4q77uKNd0f3p4mVmFaG5cIzJLv07A6Fpt\n"
          + "43C/dxC//AH2hdmoRBBYMql1GNXRor5H4idq9Joz+EkIYIvUX7Q6hL+hqkpMfT7P\n"
          + "T19sdl6gSzeRntwi5m3OFBqOasv+zbMUZBfHWymeMr/y7vrTC0LUq7dBMtoM1O/4\n"
          + "gdW7jVg/tRvoSSiicNoxBN33shbyTApOB6jtSj1etX+jkMOvJwIDAQABo2MwYTAO\n"
          + "BgNVHQ8BAf8EBAMCAYYwDwYDVR0TAQH/BAUwAwEB/zAdBgNVHQ4EFgQUA95QNVbR\n"
          + "TLtm8KPiGxvDl7I90VUwHwYDVR0jBBgwFoAUA95QNVbRTLtm8KPiGxvDl7I90VUw\n"
          + "DQYJKoZIhvcNAQEFBQADggEBAMucN6pIExIK+t1EnE9SsPTfrgT1eXkIoyQY/Esr\n"
          + "hMAtudXH/vTBH1jLuG2cenTnmCmrEbXjcKChzUyImZOMkXDiqw8cvpOp/2PV5Adg\n"
          + "06O/nVsJ8dWO41P0jmP6P6fbtGbfYmbW0W5BjfIttep3Sp+dWOIrWcBAI+0tKIJF\n"
          + "PnlUkiaY4IBIqDfv8NZ5YBberOgOzW6sRBc4L0na4UU+Krk2U886UAb3LujEV0ls\n"
          + "YSEY1QSteDwsOoBrp+uvFRTp2InBuThs4pFsiv9kuXclVzDAGySj4dzp30d8tbQk\n"
          + "CAUw7C29C79Fv1C5qfPrmAESrciIxpg0X40KPMbp1ZWVbd4=\n"
          + "-----END CERTIFICATE-----";

  @Override
  public String getEnvironmentBaseUrl() {
    return ENV_BASE_URL;
  }

  @Override
  public String getApiPrefix() {
    return API_PREFIX;
  }

  @Override
  public String getRegistrationServiceUrl() {
    return REGISTRATION_SERVICE_URL;
  }

  @Override
  public List<String> getRootCertificates() {
    return Arrays.asList(rootCertificate1, rootCertificate2, rootCertificate3);
  }
}
