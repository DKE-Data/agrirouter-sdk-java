package com.dke.data.agrirouter.impl.revoke;

import com.dke.data.agrirouter.api.dto.revoke.RevokeRequest;
import com.dke.data.agrirouter.api.dto.revoke.RevokingError;
import com.dke.data.agrirouter.api.enums.RevokeResponse;
import com.dke.data.agrirouter.api.env.Environment;
import com.dke.data.agrirouter.api.exception.RevokingException;
import com.dke.data.agrirouter.api.service.RevokingService;
import com.dke.data.agrirouter.api.service.parameters.RevokeParameters;
import com.dke.data.agrirouter.impl.EnvironmentalService;
import com.dke.data.agrirouter.impl.RequestFactory;
import com.dke.data.agrirouter.impl.SignatureService;
import com.dke.data.agrirouter.impl.common.UtcTimeService;
import com.google.gson.Gson;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;
import java.util.Optional;

public class RevokingServiceImpl extends EnvironmentalService
        implements RevokingService, SignatureService {

    public RevokingServiceImpl(Environment environment) {
        super(environment);
    }

    @Override
    public RevokeResponse revoke(RevokeParameters revokeParameters) {
        revokeParameters.validate();
        Response response = null;
        var revokeRequest = createRevokeRequestBody(revokeParameters);
        var gson = new Gson();
        var jsonBody = gson.toJson(revokeRequest).replace("\n", "");
        var encodedSignature = this.createSignature(revokeParameters, jsonBody);
        this.verifySignature(jsonBody, decodeHex(encodedSignature), revokeParameters.getPublicKey());

        try {
            response =
                    RequestFactory.signedDeleteRequest(
                                    environment.getRevokeUrl(), revokeParameters.getApplicationId(), encodedSignature)
                            .build("DELETE", Entity.entity(jsonBody, MediaType.APPLICATION_JSON_TYPE))
                            .invoke();

            response.bufferEntity();
            var result = RevokeResponse.Filter.valueOf(response.getStatus());
            if (Objects.requireNonNull(result).getKey() == RevokeResponse.SUCCESS.getKey()) {
                return result;
            } else {
                throw new RevokingException(getLastRevokingError(response.readEntity(String.class)));
            }
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }

    private String createSignature(RevokeParameters revokeParameters, String jsonBody) {
        var signature = this.createSignature(jsonBody, revokeParameters.getPrivateKey());
        return Hex.encodeHexString(signature);
    }

    private RevokeRequest createRevokeRequestBody(RevokeParameters parameters) {
        this.getNativeLogger().info("BEGIN | Create revoking request. | '{}'.", parameters);
        var revokeRequest = new RevokeRequest();
        revokeRequest.setAccountId(Objects.requireNonNull(parameters.getAccountId()));
        revokeRequest.setEndpointIds(
                Objects.requireNonNull(parameters.getEndpointIds()).toArray(new String[]{}));
        revokeRequest.setUTCTimestamp(UtcTimeService.inThePast(10).toString());
        revokeRequest.setTimeZone(UtcTimeService.offset());
        this.getNativeLogger().info("END | Create revoking request. | '{}'.", parameters);
        return revokeRequest;
    }

    public Optional<RevokingError> getLastRevokingError(String errorResponse) {
        return StringUtils.isBlank(errorResponse)
                ? Optional.empty()
                : Optional.of(failSafeGsonParsing(errorResponse));
    }

    private RevokingError failSafeGsonParsing(String error) {
        var gson = new Gson();
        try {
            return gson.fromJson(error, RevokingError.class);
        } catch (Exception e) {
            this.getNativeLogger().error("Error parsing error response. | '{}'.", error);
            return RevokingError.unknownError(error);
        }
    }
}
