package com.dke.data.agrirouter.impl.validation;

public class ResponseStatusChecker {

    public static boolean isStatusInSuccessRange(int status) {
        return status >= 200 && status <= 299;
    }
}
