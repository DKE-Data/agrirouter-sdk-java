package com.dke.data.agrirouter.impl.validation;


import com.dke.data.agrirouter.api.exception.ForbiddenRequestException;
import com.dke.data.agrirouter.api.exception.InvalidUrlForRequestException;
import com.dke.data.agrirouter.api.exception.UnauthorizedRequestException;
import com.dke.data.agrirouter.api.exception.UnexpectedHttpStatusException;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.Response;
import java.net.HttpURLConnection;
import java.net.URL;

class ResponseValidatorTest implements ResponseValidator {

    @Test
    void givenValidUrl_AssertResponseStatusIsValid_ShouldThrowNoException() throws Exception {
        Response getResponse = Response.status(this.openConnection("https://exme.net/").getResponseCode()).build();
        this.assertResponseStatusIsValid(getResponse, HttpStatus.SC_OK);
    }

    @Test
    void givenValidUrl_AssertResponseStatusIsValid_ShouldThrowUnexpectedHttpStatusExceptionIfResponseStatusDoesNotMatch() throws Exception {
        Response getResponse = Response.status(this.openConnection("https://exme.net/").getResponseCode()).build();
        Assertions.assertThrows(UnexpectedHttpStatusException.class, () -> this.assertResponseStatusIsValid(getResponse, HttpStatus.SC_CREATED));
    }

    @Test
    void givenInvalidUrl_AssertResponseStatusIsValid_ShouldThrowUnauthorizedRequestException() throws Exception {
        Response getResponse = Response.status(this.openConnection("https://exme.net/401").getResponseCode()).build();
        Assertions.assertThrows(UnauthorizedRequestException.class, () -> this.assertResponseStatusIsValid(getResponse, HttpStatus.SC_NOT_FOUND));
    }

    @Test
    void givenInvalidUrl_AssertResponseStatusIsValid_ShouldThrowForbiddenRequestException() throws Exception {
        Response getResponse = Response.status(this.openConnection("https://exme.net/403").getResponseCode()).build();
        Assertions.assertThrows(ForbiddenRequestException.class, () -> this.assertResponseStatusIsValid(getResponse, HttpStatus.SC_NOT_FOUND));
    }

    @Test
    void givenInvalidUrl_AssertResponseStatusIsValid_ShouldThrowInvalidUrlForRequestException() throws Exception {
        Response getResponse = Response.status(this.openConnection("https://exme.net/404").getResponseCode()).build();
        Assertions.assertThrows(InvalidUrlForRequestException.class, () -> this.assertResponseStatusIsValid(getResponse, HttpStatus.SC_NOT_FOUND));
    }

    private HttpURLConnection openConnection(String spec) throws Exception {
        URL url = new URL(spec);
        return (HttpURLConnection) url.openConnection();
    }

}