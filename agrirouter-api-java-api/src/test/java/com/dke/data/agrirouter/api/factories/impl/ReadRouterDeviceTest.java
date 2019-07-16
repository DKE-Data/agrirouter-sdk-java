package com.dke.data.agrirouter.api.factories.impl;

import com.google.gson.Gson;
import de.dev4agriculture.dto.RouterDevice;
import org.junit.jupiter.api.Test;

public class ReadRouterDeviceTest {

  @Test
  void givenValidRouterDeviceCanBeCreated() {
    Gson gson = new Gson();
    String resourceRouterDevice =
        "{\n"
            + "  \"authentication\": {\n"
            + "    \"type\": \"PEM\",\n"
            + "    \"secret\": \"adflaffeöl4öalefölak3pojfeföldalkj\",\n"
            + "    \"certificate\": \"-----BEGIN ENCRYPTED PRIVATE KEY-----\\nMIIE6zAdBgoqhkiG9w0BDAEDMA8ECNh4hJ/MqxfyAgMCAAAEggTIvo4itshAyfn1\\n6w2w/shWC9EMTM3R3CWL0Q9ih81mzOEVb08bYFc4x3BDYV5JsNq2qCfVdmzqIglc\\neznHlhPbXAzxx9Us5v4wvfSp5rI3n5cMNyMJ3HswGB5iEooeSMZaIsVd4ZesK3Hj\\nwqcO93ZecVGxGdTJqm4A0F8TLlCveXbk0SE94U9TBp6rZj86OaG6y5xXfawDdzZK\\nOej9Q8xhOrCM+08+yMlNEm0pgwF2/biRjjs+xQeaMZxCYMmXFUyLcLcK+49VwMLq\\nR6xRGc7+ggnqqNSG4gaP1qZPwDSq8QAVMOd74MFoad2+19dZbyxp6zzqrB/YWUdE\\nj8/xqfPKdviGp2O2/nnZh5fZ07djCULLb8Pca+j2VgeYbGoKwTHk21n68MK6UAGG\\nU6P6kkWuAt496XvLNFyR+Qs6QXYON62pajXSe+A+/t+Q5jvqk9sH5xSWPZmByh7j\\nCs1w9RLHRc768ZTghI2hEtYsAKlA7FbF8b8iHF6U6VHlvO5ETFzsvSM8XYlyKfZ1\\nulSXenee2e/Y2IrX0nmp2go57zKi5l50x6Wi/GInP7P8ZRv5Qoo6Jgna/Kpmm6+k\\njP3vWVfgODvdgnBWgwhMFsuMeyHJD9G0AEQVgHcC+gf4wm0koitE9ljxJY1SRlOA\\nJEEIE6E0T4sVZjsgqBl/JkDyUei+bZbDobOaOQz1E0YbvylOGcX0jQq7H1PT6Xei\\nDfJRSl9cCtvg/p5aI4ukDFipnenv2Uu4h+uu2n64s45dRvbk0/AvJKmKL+8TCHTU\\n9bG3nMwqWCV55iNT8A3+ZLM5Hr9IkVkJlSCCo/baUOUFV1GBJIHjK2YqL3g5htmk\\nnKRXllpr48eXecw8QOOZrLjyVeryv6oxjP/c0KNhXvAf9QtEti+wuwuqJThx+hFE\\nC+CRqvQRNKIAdvn43nLmmFUmtgtWRgH+ogAKWc9SVUjLTtrNqePCrKa+YoZDA0zS\\nOIrHROTV4yLXhMQxshEgMjnBeAl0Yuk6BwxqryJsM4sFA5h29Tx4X6MOJ/PUV9aV\\nsRtVbuiZw5tJlR+BBOld2iHJKB60L0w/JosjAR+i44HWmH/ZRoMCPUpKwZbwEKzZ\\nc2rlFC1NjWjMTWxCNILoIRUSOF2Lz6Sr+IbpFOUY6MWXlC6yocddB0Il0x7uCxUW\\nV0HYPzV+Kr8x0X+4fqqhZU5Jc6EyObsZCKuLEmw3WKhtgexvtItev6lzlQxwUEYH\\ny15m4wAwZmvGwfUXcvPlHVkt31conDY6oAenv6iDTanOvk+fZGbRq78e11/Lh4Hl\\nbfCimv8gAfnt8gKSaY1v2uqgInYF7lqrAdlv1I1uUUJxjRZcNieOAbmWDQG7v6w+\\nxMV4eaRQ8tml6GwohOUmQPCU8lKxyUXO9rZmZu835qvhr5brOqE1Liw6cfef1s1B\\n+lohBgBFcPSauQpkfRqVoFe1JzoCEsg6h20VoG9+j5NWPRwwIRT72qP46Z0zA+lK\\ntmGSVgQXKSAvllu62JAFNL/bCmGJdfOTbn2aCVr1T2LU0DICxPG473Og0kOP1bK0\\nYFsB9hB1m6V2ZYHE5quOWxN/Qa+fe/P9b7/1xtxHlYguNMbvNIAE3frsm+lSV09v\\n8nidTRG8BzqvtMj6ylx8\\n-----END ENCRYPTED PRIVATE KEY-----\\n-----BEGIN CERTIFICATE-----\\nMIIEZzCCA0+gAwIBAgIPAN5CrgOURNoAEAECavnKMA0GCSqGSIb3DQEBCwUAMFYx\\nCzAJBgNVBAYTAkRFMSMwIQYDVQQKExpTQVAgSW9UIFRydXN0IENvbW11bml0eSBJ\\nSTEiMCAGA1UEAxMZU0FQIEludGVybmV0IG9mIFRoaW5ncyBDQTAeFw0xOTA3MTMx\\nOTQzNTVaFw0yMDA3MTMxOTQzNTVaMIG0MQswCQYDVQQGEwJERTEcMBoGA1UEChMT\\nU0FQIFRydXN0IENvbW11bml0eTEVMBMGA1UECxMMSW9UIFNlcnZpY2VzMXAwbgYD\\nVQQDFGdkZXZpY2VBbHRlcm5hdGVJZDpjOTQzZDkxZi1lZGFhLTRiNzgtYTBiMi1h\\nYTFkMzRmODM2NzJ8Z2F0ZXdheUlkOjJ8dGVuYW50SWQ6ODA1Mzk5MjgxfGluc3Rh\\nbmNlSWQ6ZGtlLXFhMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnTgN\\nt0N0N+cd24I6QrgF3g52XYa7Yg7O0wSBS9tmGJYC8VEyJgYGy+75Pwp/SpAKy9fw\\nfL1CO2z+ljnmWscqHbkmJhrydK6ZcXh2UztsPhyT6xJ8cHaoXtYVJiVuRGzt9c2G\\nHkrMUIaaE2e303CsKxDhcJZwQZbHqbDKdqy1DVoakiNfgOtEvo9/ZW/FBd01y74b\\nX+cxzWMNsk74PYrLwytSpQVoTHMKZSn7i4We8DXde6XZpC2o1njB9meZMMEaOF8b\\nMk3njvRXdkUtn1RiyVwMCEwq1LZ3zqFNuYmCvBVaj9RE+kQL+BWvyvyJPyDrPlwr\\n1Krcog0XUnjGW3toZQIDAQABo4HSMIHPMEgGA1UdHwRBMD8wPaA7oDmGN2h0dHBz\\nOi8vdGNzLm15c2FwLmNvbS9jcmwvVHJ1c3RDb21tdW5pdHlJSS9TQVBJb1RDQS5j\\ncmwwDAYDVR0TAQH/BAIwADAlBgNVHRIEHjAchhpodHRwOi8vc2VydmljZS5zYXAu\\nY29tL1RDUzAOBgNVHQ8BAf8EBAMCBsAwHQYDVR0OBBYEFPXvUSwiy2r5U8W0paTi\\n4IwpOu8nMB8GA1UdIwQYMBaAFJW3s/VY3tW0s1hG4PKmyXhOvS11MA0GCSqGSIb3\\nDQEBCwUAA4IBAQAezQNSXnt8O8S36ks/k4/p6WtghPLQkq/+Py97q6jBZITlnfXN\\ns2dRbEaW1oqeP3g0FhbE4Gpdt6foh6nQhcFSxxxVp2yk6kc9pd75mL5KMMz+xKBP\\nxsW7D2aA9spatJQBqOaBDlUGIL+7ZNXcpGGNbHCtQ77Hxx73XzhogjcIPKb7JKZg\\nu06Uc4L7BNY92twG2TxqKO2actd3XBVb0D2CSCSXQJW8xa5mHaj09l8Sl9eqhYyo\\nVzEXzOVwWYm5rqrUj7IqNm/UHfFdcYapQRMNNU5+5uUL45Sda69CCZdX2jhjtYw\\nHJbYUnUOGTUgXE9QF7JkCEf8XnulNAmtcNYY\\n-----END CERTIFICATE-----\\n\"\n"
            + "  },\n"
            + "  \"deviceAlternateId\": \"c943d91f-edaa-4b78-a0b2-adfabbfbdb34fa3\",\n"
            + "  \"connectionCriteria\": {\n"
            + "    \"clientId\": \"31593067-88a3-44e3-b5b4-abdeafefaefe\",\n"
            + "    \"gatewayId\": \"2\",\n"
            + "    \"host\": \"dke-qa.eu10.cp.iot.sap\",\n"
            + "    \"port\": 8883\n"
            + "  }\n"
            + "}";
    RouterDevice routerDevice = gson.fromJson(resourceRouterDevice, RouterDevice.class);
  }
}
