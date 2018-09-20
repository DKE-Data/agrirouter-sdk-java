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
   * @param checkRange If a range check should be performed
   */
  default void assertResponseStatusIsValid(Response response, int exceptedHttpStatus, boolean checkRange) {
    LOGGER.debug("Validating response.");
    LOGGER.trace(new ObjectArrayMessage(response, exceptedHttpStatus, checkRange));

    int actualHttpStatus = this.getStatus(response);
    if (checkRange) {
      this.assertResponseStatusIsValidWithRange(actualHttpStatus, HTTP_STATUS_OK_MIN, HTTP_STATUS_OK_MAX);
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
   */
  default void assertResponseStatusIsValid(Response response, int exceptedHttpStatus) {
    LOGGER.debug("Validating response.");
    LOGGER.trace(new ObjectArrayMessage(response, exceptedHttpStatus));

    int actualHttpStatus = this.getStatus(response);
    this.assertResponseStatusIsValidWithoutRange(actualHttpStatus, exceptedHttpStatus);
  }

  /**
   * Will assert, that the response status is valid. If there will be an 404 or 401 a business
   * exception will rise.
   *
   * @param response The current response.
   * @param exceptedHttpStatus The expected HTTP status.
   */
  default void assertResponseStatusIsValid(WebResponse response, int exceptedHttpStatus) {
    LOGGER.debug("Validating response.");
    LOGGER.trace(new ObjectArrayMessage(response, exceptedHttpStatus));

    int actualHttpStatus = this.getStatus(response);
    this.assertResponseStatusIsValidWithoutRange(actualHttpStatus, exceptedHttpStatus);
  }

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
   * Will assert, that the response status is within a valid range. If there will be an 404 or 401 a business
   * exception will rise.
   *
   * @param response The current response.
   * @param expectedMinimumState the minimum allowed value
   * @param expectedMaximumState the maximum value allowed
   */
  default void assertResponseStatusIsInRange(Response response, int expectedMinimumState, int expectedMaximumState){
    LOGGER.debug("Validating response in Range.");
    LOGGER.trace(new ObjectArrayMessage(response, expectedMinimumState,expectedMaximumState));

    int status = this.getStatus(response);
    this.assertResponseStatusIsValidWithRange(status, expectedMinimumState, expectedMaximumState);
  }

  /**
   * Will assert, that the response status is within a valid range. If there will be an 404 or 401 a business
   * exception will rise.
   *
   * @param response The current response.
   * @param expectedMinimumState the minimum allowed value
   * @param expectedMaximumState the maximum value allowed
   */
  default void assertResponseStatusIsInRange(WebResponse response, int expectedMinimumState, int expectedMaximumState){
    LOGGER.debug("Validating response in Range.");
    LOGGER.trace(new ObjectArrayMessage(response, expectedMinimumState,expectedMaximumState));

    int status = this.getStatus(response);
    this.assertResponseStatusIsValidWithRange(status, expectedMinimumState, expectedMaximumState);
  }

  default void assertResponseStatusIsValidWithRange(int actualStatus, int expectedMinimumState, int expectedMaximumState) {
    if (actualStatus == HttpStatus.SC_NOT_FOUND) {
      throw new InvalidUrlForRequestException();
    }
    if (actualStatus == HttpStatus.SC_UNAUTHORIZED) {
      throw new UnauthorizedRequestException();
    }
    if (actualStatus == HttpStatus.SC_FORBIDDEN) {
      throw new ForbiddenRequestException();
    }
    if (actualStatus < expectedMinimumState) {
      throw new UnexpectedHttpStatusException(actualStatus, expectedMinimumState);
    } else if (actualStatus > expectedMaximumState) {
      throw new UnexpectedHttpStatusException(actualStatus, expectedMinimumState);
    }
  }


  default int getStatus(WebResponse response) {
    return response.getStatusCode();
  }

  default int getStatus(Response response) {
    return response.getStatus();
  }
}
