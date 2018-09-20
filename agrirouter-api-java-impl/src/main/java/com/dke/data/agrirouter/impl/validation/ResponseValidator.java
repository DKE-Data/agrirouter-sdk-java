package com.dke.data.agrirouter.impl.validation;

import com.dke.data.agrirouter.api.exception.ForbiddenRequestException;
import com.dke.data.agrirouter.api.exception.InvalidUrlForRequestException;
import com.dke.data.agrirouter.api.exception.UnauthorizedRequestException;
import com.dke.data.agrirouter.api.exception.UnexpectedHttpStatusException;
import com.gargoylesoftware.htmlunit.WebResponse;
import javax.ws.rs.core.Response;
import org.apache.http.HttpStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.ObjectArrayMessage;

/** Validation of the response, encapsulated using an interface. */
public interface ResponseValidator {

  int HTTP_STATUS_OK_MIN = 200;
  int HTTP_STATUS_OK_MAX = 299;

  Logger LOGGER = LogManager.getLogger();

  /**
   * Will assert, that the response status is valid. If there will be an 404 or 401 a business
   * exception will rise.
   *
   * @param response The current response.
   * @param exceptedHttpStatus The expected HTTP status.
   * @param checkRange If any status code between <code>HTTP_STATUS_OK_MIN</code> and <code>HTTP_STATUS_OK_MAX</code>
   *                   should be interpreted as valid HTTP_OK status
   */
  default void assertResponseStatusIsValid(Response response, int exceptedHttpStatus, boolean checkRange) {
    LOGGER.debug("Validating response.");
    LOGGER.trace(new ObjectArrayMessage(response, exceptedHttpStatus, checkRange));

    int actualHttpStatus = this.getStatus(response);
    if (checkRange) {
      this.assertResponseStatusIsValidWithRange(actualHttpStatus);
    } else {
      this.assertResponseStatusIsValidWithoutRange(actualHttpStatus, exceptedHttpStatus);
    }
  }

  /**
   * Will assert, that the response status is valid. If there will be an 404 or 401 a business
   * exception will rise.
   *
   * @param response The current response.
   * @param exceptedHttpStatus The expected HTTP status.
   * @param checkRange If any status code between <code>HTTP_STATUS_OK_MIN</code> and <code>HTTP_STATUS_OK_MAX</code>
   *                   should be interpreted as valid HTTP_OK status
   */
  default void assertResponseStatusIsValid(WebResponse response, int exceptedHttpStatus, boolean checkRange) {
    LOGGER.debug("Validating response.");
    LOGGER.trace(new ObjectArrayMessage(response, exceptedHttpStatus, checkRange));

    int actualHttpStatus = this.getStatus(response);
    if (checkRange) {
      this.assertResponseStatusIsValidWithRange(actualHttpStatus);
    } else {
      this.assertResponseStatusIsValidWithoutRange(actualHttpStatus, exceptedHttpStatus);
    }
  }

  /**
   * Helper method to check if response status code is valid.
   *
   * @param actualStatus The actual HTTP status of the response
   * @param expectedStatus The expected HTTP status
   */
  default void assertResponseStatusIsValidWithoutRange(int actualStatus, int expectedStatus) {
    if (actualStatus == HttpStatus.SC_NOT_FOUND) {
      throw new InvalidUrlForRequestException();
    }
    if (actualStatus == HttpStatus.SC_UNAUTHORIZED) {
      throw new UnauthorizedRequestException();
    }
    if (actualStatus == HttpStatus.SC_FORBIDDEN) {
      throw new ForbiddenRequestException();
    }
    if (actualStatus != expectedStatus) {
      throw new UnexpectedHttpStatusException(actualStatus, expectedStatus);
    }
  }

  /**
   * Helper method to check if response status code is within the valid range <code>HTTP_STATUS_OK_MIN</code> and
   * <code>HTTP_STATUS_OK_MAX</code>
   *
   * @param actualStatus The actual HTTP status of the response
   */
  default void assertResponseStatusIsValidWithRange(int actualStatus) {
    if (actualStatus == HttpStatus.SC_NOT_FOUND) {
      throw new InvalidUrlForRequestException();
    }
    if (actualStatus == HttpStatus.SC_UNAUTHORIZED) {
      throw new UnauthorizedRequestException();
    }
    if (actualStatus == HttpStatus.SC_FORBIDDEN) {
      throw new ForbiddenRequestException();
    }
    if (actualStatus < HTTP_STATUS_OK_MIN) {
      throw new UnexpectedHttpStatusException(actualStatus, HTTP_STATUS_OK_MIN);
    } else if (actualStatus > HTTP_STATUS_OK_MAX) {
      throw new UnexpectedHttpStatusException(actualStatus, HTTP_STATUS_OK_MAX);
    }
  }

  default int getStatus(WebResponse response) {
    return response.getStatusCode();
  }

  default int getStatus(Response response) {
    return response.getStatus();
  }
}
