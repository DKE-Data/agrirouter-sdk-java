package com.dke.data.agrirouter.impl.messaging;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.dke.data.agrirouter.api.dto.onboard.OnboardingResponse;
import com.dke.data.agrirouter.api.dto.onboard.inner.Authentication;
import com.dke.data.agrirouter.api.dto.onboard.inner.ConnectionCriteria;
import com.dke.data.agrirouter.api.enums.CertificationType;
import com.dke.data.agrirouter.api.exception.CouldNotCreateDynamicKeyStoreException;
import com.dke.data.agrirouter.api.exception.IllegalParameterDefinitionException;
import com.dke.data.agrirouter.api.service.parameters.SendMessageParameters;
import com.dke.data.agrirouter.impl.messaging.rest.SendMessageServiceImpl;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;

class SendMessageServiceImplTest {

  private static final String CERTIFICATE_CERTIFICATE =
      "-----BEGIN ENCRYPTED PRIVATE KEY-----\n"
          + "MIIE6zAdBgoqhkiG9w0BDAEDMA8ECAPZAuXegGbwAgMCAAAEggTIIvYLpF6TVV1j\n"
          + "cJr16cCcg9bh8DGTgNkBLJ11dyJRSh54R39OKg0C0VxGThbLDE+Br6tlPsMvqI3V\n"
          + "ylpKNR9A4cduEJPMlgfSS5SFs21+NRjC/0Mrq7FL9DhCL4/BADOpky6DRYWU6wZE\n"
          + "MV2QChJpfYu4QeTvBjmoDkicvT/g3iAwW0xFyF2uS9EnwYPW2vVAFjYhUBJ/9IO/\n"
          + "UYJ8wJh1zIsExEd3VmKp6A7XW9PhtRtmaHHlZe7iz6CUfZHhGqiig6cGT4IbIazy\n"
          + "b732rmvW1CVBs/BfyELnX0lLjuFW3aiLKib2rIhEo6VtvYfWp1X4sp/l9QVupCMb\n"
          + "cLR3t2CO7CmIcl7U7AlDJf4O2NeidoYzLYxJBjJfyH5FaWripkv27pYMcWLeVJ8F\n"
          + "UHEK8O3z/1wNBW11T0nmVaP/YPGOed1J69ct6V8vQ9BAyW42UXxc3eSVAMIpISkP\n"
          + "39YpeEO6Zb2WgEXIub5+2MvuXghR8KVQxE5Tz2juqi+EgU+gmzZuJbmNvQgtKc3/\n"
          + "/EEABFJoEMVjcb192G05+RoJLjqLs47HnQ/UnUv13cjx8ZIo3pVgQZFgHRJztNBo\n"
          + "eAXsCNg6vdsFH2SREw/uatLqbkRaVgtNjpYcdsE3X/+PTqwarh0Mmx/Z7FKNEuPB\n"
          + "vtVumXeN+R6T2LpFf91jRMMWOuDN+iJRQW7ALlbfvonTClSqXvlqf8jJZjnZ9vAJ\n"
          + "oE3QCcBAvXnuscpCH7XxnP4rEkjAb9AdFI2bHs747f2+o4+kqjNk/8btcpq1smV+\n"
          + "XSI0YllG7RGuzdQcF2J2yqgDM+T0amWIicv3wSpYJikelyt6eaCHWOzjT27n8jVh\n"
          + "nGs8WyT1NLd55JSbLA05OTzZfmbGm6hzyf2dGNX3/clFYupYAdHSPXHUYogIxZ/N\n"
          + "qNT23q0wLjxBdy8u8pE/ZHWCWTTHtI51/e+kYkd8nueleWh7YmX24h5gIfpxmBqO\n"
          + "NzTEvqtXbaRR84h9U7yjjkEnHSJphrSnOgxL5MMBUb212ar0kXkBNAzANcaHyK6X\n"
          + "LxLu1J1/jpyh5zZbNjaG5toEIkoF9+x5DNu6InKPrzvI3u0bP1eQoUKl45ywG0e9\n"
          + "bafkVonZxAQINC45CjwJcFIIxVjxT7QRJNQ1Dy+uZLa2Nh5sB0RNQ6HhMPVF/6jl\n"
          + "1G7fYJgSOVMy1CHVFR0JFYJCkowTVjZX4xxv7ico3+yaI8oaBj23xiKQJKmaTonF\n"
          + "afdvuvpSp9Bu5p8ziGOdFmrGEU+vy6bp+foU2MpsYfCRtPwW8fKsjGiyrcwk98Wo\n"
          + "auLDrNb38KxAVaXdgqqInuzpS6ECRSnWZ/etOv3Eo1y3dAddrQu1dX7PXK6LXcxi\n"
          + "jn4YhxggYJmzUrQZOKWjj9MAvscWNbgY1cedoPTy9lfoxmmdR1gnIqbzBfE9MOgL\n"
          + "//lk5EgxOQg0LjYwS6qH6OnlFcSffLKoaJwswfD9bGPaYO8z458UW0Mg9vV7Xh/4\n"
          + "ZzC/sVSP3UDw/gDT43ldzMNU4vJ9MzjLYUdPbx47gi1QtpnExdzOF6ox1g1JIr9N\n"
          + "7XNYSPcTTEHUgoZpbqjJnSQiOTeYsjPDSbJfLIAD+zsUt40vt4sSd5B3hKQ5BZB/\n"
          + "LbRs59f9S2ZElRLw6hBA\n"
          + "-----END ENCRYPTED PRIVATE KEY-----\n";

  private static final String CERTIFICATE_PRIVATEKEY =
      "-----BEGIN CERTIFICATE-----\n"
          + "MIIEPzCCAyegAwIBAgIOYxFUp0BtW2EQAQIAX1wwDQYJKoZIhvcNAQELBQAwVjEL\n"
          + "MAkGA1UEBhMCREUxIzAhBgNVBAoTGlNBUCBJb1QgVHJ1c3QgQ29tbXVuaXR5IElJ\n"
          + "MSIwIAYDVQQDExlTQVAgSW50ZXJuZXQgb2YgVGhpbmdzIENBMB4XDTE4MDIyMDEx\n"
          + "MDIwN1oXDTE5MDIyMDExMDIwN1owgY0xCzAJBgNVBAYTAkRFMRwwGgYDVQQKExNT\n"
          + "QVAgVHJ1c3QgQ29tbXVuaXR5MRUwEwYDVQQLEwxJb1QgU2VydmljZXMxSTBHBgNV\n"
          + "BAMUQGNsaWVudGlkOmUxOWFhNTVjLTk5OGItNDJkYi1hMjIzLTQxZTQzNmQ5YmIz\n"
          + "YnxpbnN0YW5jZWlkOmRrZS1kZXYwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEK\n"
          + "AoIBAQCdvPPeF1Lyww3Jm3+4bMwl4qBj6j13tF6SKaiaysXm97gg9rhE9LP0jpXQ\n"
          + "F7FdutSs/BlxlP04AnJbwiDdTR3L/VtwuuG0j5q4tFeDrFLQjbf48Kw3EoMaR/cX\n"
          + "oiU936obpeuK96s5DVcUWI3WtMeZTp8vptKQM2n+EkIFT03CJ5SqheXVSm0B/mpI\n"
          + "fJQWVqJa0C4m8LHDRXWSAm2GWNWHDyRG92w2kPyWQ2rDXX8XkvOknLSSVKAaMa8I\n"
          + "QOSHgVIapLC1crlSpBfLx30jNijAQLcqmXY6DrgD+HrH5KjmwkhUsotdrXAgpXUq\n"
          + "3NGdZnpgWm4iixtUvZqEmSjw76dRAgMBAAGjgdIwgc8wSAYDVR0fBEEwPzA9oDug\n"
          + "OYY3aHR0cHM6Ly90Y3MubXlzYXAuY29tL2NybC9UcnVzdENvbW11bml0eUlJL1NB\n"
          + "UElvVENBLmNybDAMBgNVHRMBAf8EAjAAMCUGA1UdEgQeMByGGmh0dHA6Ly9zZXJ2\n"
          + "aWNlLnNhcC5jb20vVENTMA4GA1UdDwEB/wQEAwIGwDAdBgNVHQ4EFgQUVzvDsKZw\n"
          + "9t+KfTDAvuN3xJ/acx8wHwYDVR0jBBgwFoAUlbez9Vje1bSzWEbg8qbJeE69LXUw\n"
          + "DQYJKoZIhvcNAQELBQADggEBADwp9ibYhOBZn3pO8BJyzlmPFqlmrd3ud1EYsT84\n"
          + "lrHtJPkPxoQISmFYZjExy9iPLOOtXMeQ2DqauLlZJ9avnGQFATJ7w/v8fMwO52b3\n"
          + "jf9iyBHzc9Wyhb8N2itGj90w5bkUSp7JtULxxH5nJAB3qF6Ct3Q8dhwTsqPl06vx\n"
          + "p2mkJY3Pjd04UIF6tR93MiAxoKmiPjsRghihUATe30JoQmPadcZcJjQ3k1A9EpcL\n"
          + "+wv3deRvXFiERDC4wCvVfkXImjVkhe1TcqSunGmFJyj5CuJ+iYNBHmiugJhvb/y/\n"
          + "mb79QPIoiHOpEsETUHyMHrmAe/F1ABO52o0BxcwjEXb3q5E=\n"
          + "-----END CERTIFICATE-----";

  private static final String MEASURES =
      "https://dke-dev.eu10.cp.iot.sap/iot/gateway/rest/measures/e19aa55c-998b-42db-a223-41e436d9bb3b";
  private static final String SENSORALTERNATE_ID = "5e26f0e1-856f-4d93-a839-68c2d973f5c1";
  private static final String CAPABILITYALTERNATE_ID = "81ce3fd5-2f70-4270-ad15-1689ab6971bf";
  private static final String PASSWORD = "NbEJai3BwcOXLi77";

  @Test
  void rq43givenNullParamsSendShouldThrowException() {
    SendMessageServiceImpl sendMessageService = new SendMessageServiceImpl();
    assertThrows(NullPointerException.class, () -> sendMessageService.send(null));
  }

  @Test
  void rq43givenEmptyParamsSendShouldThrowException() {
    SendMessageServiceImpl sendMessageService = new SendMessageServiceImpl();
    SendMessageParameters sendMessageParameters = new SendMessageParameters();
    assertThrows(
        IllegalParameterDefinitionException.class,
        () -> sendMessageService.send(sendMessageParameters));
  }

  @Test
  void rq43givenEmptyCertificateSendShouldThrowException() {
    SendMessageServiceImpl sendMessageService = new SendMessageServiceImpl();
    SendMessageParameters sendMessageParameters = this.createDefaultParameters();
    sendMessageParameters.getOnboardingResponse().getAuthentication().setCertificate("");
    assertThrows(
        CouldNotCreateDynamicKeyStoreException.class,
        () -> sendMessageService.send(sendMessageParameters));
  }

  @Test
  @SuppressWarnings("ConstantConditions")
  void rq43givenNullCertificateSendShouldThrowException() {
    SendMessageServiceImpl sendMessageService = new SendMessageServiceImpl();
    SendMessageParameters sendMessageParameters = this.createDefaultParameters();
    sendMessageParameters.getOnboardingResponse().getAuthentication().setCertificate(null);
    assertThrows(
        IllegalParameterDefinitionException.class,
        () -> sendMessageService.send(sendMessageParameters));
  }

  @Test
  void rq43givenCertificateWithMissingPrivateKeySendShouldThrowException() {
    SendMessageServiceImpl sendMessageService = new SendMessageServiceImpl();
    SendMessageParameters sendMessageParameters = this.createDefaultParameters();
    sendMessageParameters
        .getOnboardingResponse()
        .getAuthentication()
        .setCertificate(CERTIFICATE_CERTIFICATE);
    assertThrows(
        CouldNotCreateDynamicKeyStoreException.class,
        () -> sendMessageService.send(sendMessageParameters));
  }

  @Test
  void rq43givenCertificateOnlyPrivateKeySendShouldThrowException() {
    SendMessageServiceImpl sendMessageService = new SendMessageServiceImpl();
    SendMessageParameters sendMessageParameters = this.createDefaultParameters();
    sendMessageParameters
        .getOnboardingResponse()
        .getAuthentication()
        .setCertificate(CERTIFICATE_PRIVATEKEY);
    assertThrows(
        CouldNotCreateDynamicKeyStoreException.class,
        () -> sendMessageService.send(sendMessageParameters));
  }

  @Test
  void rq43givenWrongCertificationTypeSendShouldThrowException() {
    SendMessageServiceImpl sendMessageService = new SendMessageServiceImpl();
    SendMessageParameters sendMessageParameters = this.createDefaultParameters();
    sendMessageParameters
        .getOnboardingResponse()
        .getAuthentication()
        .setType(CertificationType.P12.getKey());
    assertThrows(
        CouldNotCreateDynamicKeyStoreException.class,
        () -> sendMessageService.send(sendMessageParameters));
  }

  private SendMessageParameters createDefaultParameters() {
    SendMessageParameters sendMessageParameters = new SendMessageParameters();
    OnboardingResponse onboardingResponse = new OnboardingResponse();

    Authentication authentication = new Authentication();
    authentication.setCertificate(
        SendMessageServiceImplTest.CERTIFICATE_PRIVATEKEY
            + SendMessageServiceImplTest.CERTIFICATE_CERTIFICATE);
    authentication.setSecret(SendMessageServiceImplTest.PASSWORD);
    authentication.setType(CertificationType.PEM.getKey());

    ConnectionCriteria connectionCriteria = new ConnectionCriteria();
    connectionCriteria.setMeasures(SendMessageServiceImplTest.MEASURES);

    onboardingResponse.setAuthentication(authentication);
    onboardingResponse.setConnectionCriteria(connectionCriteria);
    onboardingResponse.setSensorAlternateId(SendMessageServiceImplTest.SENSORALTERNATE_ID);
    onboardingResponse.setCapabilityAlternateId(SendMessageServiceImplTest.CAPABILITYALTERNATE_ID);

    sendMessageParameters.setOnboardingResponse(onboardingResponse);
    sendMessageParameters.setEncodedMessages(new ArrayList<>());
    return sendMessageParameters;
  }
}
