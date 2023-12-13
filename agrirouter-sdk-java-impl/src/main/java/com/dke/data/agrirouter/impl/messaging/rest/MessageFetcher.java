package com.dke.data.agrirouter.impl.messaging.rest;

import com.dke.data.agrirouter.api.cancellation.CancellationToken;
import com.dke.data.agrirouter.api.enums.CertificationType;
import com.dke.data.agrirouter.api.service.HasLogger;
import com.dke.data.agrirouter.api.service.parameters.FetchMessageParameters;
import com.dke.data.agrirouter.impl.RequestFactory;
import com.dke.data.agrirouter.impl.validation.ResponseValidator;
import jakarta.ws.rs.core.Response;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;
import java.util.Optional;

/**
 * Interface to fetch messages for the HTTP implementation by polling the outbox.
 */
public interface MessageFetcher extends ResponseValidator, HasLogger {

    int MAX_TRIES_BEFORE_FAILURE = 10;
    long DEFAULT_INTERVAL = 500;

    String EMPTY_CONTENT = "[]";

    /**
     * Poll for new messages using the given parameters.
     *
     * @param fetchMessageParameters -
     * @param cancellationToken      Token to manage the whole polling process.
     * @return Response from the outbox, if existing.
     */
    default Optional<String> poll(
            FetchMessageParameters fetchMessageParameters, CancellationToken cancellationToken) {
        fetchMessageParameters.validate();
        int nrOfTries = 1;
        while (cancellationToken.isNotCancelled()) {
            getNativeLogger()
                    .debug(
                            "The cancellation token is not cancelled, we have another try. This is try number {}.",
                            nrOfTries);
            Response response =
                    RequestFactory.securedRequest(
                                    Objects.requireNonNull(fetchMessageParameters.getOnboardingResponse())
                                            .getConnectionCriteria()
                                            .getCommands(),
                                    fetchMessageParameters
                                            .getOnboardingResponse()
                                            .getAuthentication()
                                            .getCertificate(),
                                    fetchMessageParameters.getOnboardingResponse().getAuthentication().getSecret(),
                                    CertificationType.valueOf(
                                            fetchMessageParameters.getOnboardingResponse().getAuthentication().getType()))
                            .get();
            this.assertStatusCodeIsOk(response.getStatus());
            String entityContent = response.readEntity(String.class);
            if (!StringUtils.equalsIgnoreCase(entityContent, EMPTY_CONTENT)) {
                return Optional.of(entityContent);
            }
            cancellationToken.nextStep();
            cancellationToken.waitIfNotCancelled();
        }
        return Optional.empty();
    }
}
