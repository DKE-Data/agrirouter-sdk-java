package com.dke.data.agrirouter.api.exception;

/**
 * Will be thrown if there was an HTTP status 401 which can indicate invalid client id and secret,
 * missing bearer token or an invalid SSL cert.
 */
public class UnauthorizedRequestException extends Exception {}
