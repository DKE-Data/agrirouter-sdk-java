package com.dke.data.agrirouter.api.env;

import java.util.Arrays;
import java.util.List;

/** Abstraction of the QA environment, currently no overrides because the default is QA already. */
public abstract class QA implements Environment {

  private static final String ENV_BASE_URL = "https://agrirouter-qa.cfapps.eu10.hana.ondemand.com";
  private static final String API_PREFIX = "/api/v1.0";
  private static final String REGISTRATION_SERVICE_URL =
      "https://agrirouter-registration-service-hubqa-eu10.cfapps.eu10.hana.ondemand.com";

  private String rootCertificate1 =
      "-----BEGIN CERTIFICATE-----\n"
          + "MIIGRjCCBS6gAwIBAgIQDzr5pfsWYv+RwbJKMtV1iDANBgkqhkiG9w0BAQsFADBN\n"
          + "MQswCQYDVQQGEwJVUzEVMBMGA1UEChMMRGlnaUNlcnQgSW5jMScwJQYDVQQDEx5E\n"
          + "aWdpQ2VydCBTSEEyIFNlY3VyZSBTZXJ2ZXIgQ0EwHhcNMTgwNDEwMDAwMDAwWhcN\n"
          + "MTkwNTA2MTIwMDAwWjCBgDELMAkGA1UEBhMCREUxETAPBgNVBAcTCFdhbGxkb3Jm\n"
          + "MQwwCgYDVQQKEwNTQVAxNjA0BgNVBAsTLVNBUCBDbG91ZCBQbGF0Zm9ybSBJbnRl\n"
          + "cm5ldCBvZiBUaGluZ3MgU2VydmljZTEYMBYGA1UEAxMPZXUxMC5jcC5pb3Quc2Fw\n"
          + "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA69/KpEHvKTI/Iv/Y2H/J\n"
          + "DpgJ8h81SNqrFzUgrMQwTXYg31Vzjoalt3VRnF484xkAeyk/4TPx5bEeCao8cLaL\n"
          + "Z8rD9ywANTdbXfnFbCKM2Bnf8cY3BDeEGr5rLGlti9YLfPiaTbAPpq4uU4sqgbSl\n"
          + "taDKj69GezXOKeK1mF0MKm1nvHGftHt0ohyPU7BW18rT3sKiViN7kIuQnlQHiiLN\n"
          + "miv+Yfnpp4fzhyyKW5A0ogQgMZrWo0tsVXMX/+L8jtnzRFS+r0cQj+felpr0RtzQ\n"
          + "HrJCpmzy6AgeYQfUqNiWIyKJi+tcrq5CV2YKFEM/ZdQKGmQ9opJvvdodupvH3gxq\n"
          + "+QIDAQABo4IC7DCCAugwHwYDVR0jBBgwFoAUD4BhHIIxYdUvKOeNRji0LOHG2eIw\n"
          + "HQYDVR0OBBYEFP980o1dmGvuM1iVg6Ms0cHZJcPhMC0GA1UdEQQmMCSCESouZXUx\n"
          + "MC5jcC5pb3Quc2Fwgg9ldTEwLmNwLmlvdC5zYXAwDgYDVR0PAQH/BAQDAgWgMB0G\n"
          + "A1UdJQQWMBQGCCsGAQUFBwMBBggrBgEFBQcDAjBrBgNVHR8EZDBiMC+gLaArhilo\n"
          + "dHRwOi8vY3JsMy5kaWdpY2VydC5jb20vc3NjYS1zaGEyLWc2LmNybDAvoC2gK4Yp\n"
          + "aHR0cDovL2NybDQuZGlnaWNlcnQuY29tL3NzY2Etc2hhMi1nNi5jcmwwTAYDVR0g\n"
          + "BEUwQzA3BglghkgBhv1sAQEwKjAoBggrBgEFBQcCARYcaHR0cHM6Ly93d3cuZGln\n"
          + "aWNlcnQuY29tL0NQUzAIBgZngQwBAgIwfAYIKwYBBQUHAQEEcDBuMCQGCCsGAQUF\n"
          + "BzABhhhodHRwOi8vb2NzcC5kaWdpY2VydC5jb20wRgYIKwYBBQUHMAKGOmh0dHA6\n"
          + "Ly9jYWNlcnRzLmRpZ2ljZXJ0LmNvbS9EaWdpQ2VydFNIQTJTZWN1cmVTZXJ2ZXJD\n"
          + "QS5jcnQwCQYDVR0TBAIwADCCAQIGCisGAQQB1nkCBAIEgfMEgfAA7gB1AKS5CZC0\n"
          + "GFgUh7sTosxncAo8NZgE+RvfuON3zQ7IDdwQAAABYq/hD+UAAAQDAEYwRAIgDj++\n"
          + "Cfxb6bxa3uQ6csqek0qOE6j7JKvOLb9loEMtCfQCIHoRNdSSNnaj6fKa5J66/JIi\n"
          + "VfZNdSfM6RzA83qU5FrLAHUAb1N2rDHwMRnYmQCkURX/dxUcEdkCwQApBo2yCJo3\n"
          + "2RMAAAFir+ERUgAABAMARjBEAiBtGv1Wjb40fI7hKO5HaOdpmz+oYy7Kjr4iVOdw\n"
          + "RuXFGgIgGamOKBzYDqIQQ+Pk4vfYYSE9bSJWOr2904y3gr0W0gkwDQYJKoZIhvcN\n"
          + "AQELBQADggEBAKk4kyOTF90gsnoG2haACf0wPiLHKxL5hjJSYQNdBhPM+RoK9BvD\n"
          + "aH4ej571d4fD/b/J2BsQUpFzCCV2Z6XUqiBoUvOMTig7qP2wYxsrBMYLKeBVUhgg\n"
          + "QkQ+QmRPOFhbVn/vq2IxunzFUQyXtasoc8nn0cLciwXjLNEBJliAaEkN6gLLnPGP\n"
          + "GGafu/q/jrREEel6w7pTahmxvGdcy3wuwev5GxEPqsoMxaiOgEZdksaCz1GkancS\n"
          + "3ZOpa9bhaFZW2Evpr8E1Gkbmykz3ffaD9IGxDsfaFphOCpU4pdWbzVEM9nuce7aA\n"
          + "k9TyDMS9b8BqnBjwhygvlBs0biRc/A3oDy0=\n"
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
