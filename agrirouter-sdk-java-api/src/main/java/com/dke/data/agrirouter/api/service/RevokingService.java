package com.dke.data.agrirouter.api.service;

import com.dke.data.agrirouter.api.dto.revoke.RevokingError;
import com.dke.data.agrirouter.api.enums.RevokeResponse;
import com.dke.data.agrirouter.api.service.parameters.RevokeParameters;

import java.util.Optional;

public interface RevokingService {

    RevokeResponse revoke(RevokeParameters revokeParameters);

    /**
     * The last error as RevokingError-Object, if the last revoking failed as object read from the
     * JSON String
     *
     * @return The last error as RevokingError-Object
     */
    Optional<RevokingError> getLastRevokingError(String errorResponse);
}
