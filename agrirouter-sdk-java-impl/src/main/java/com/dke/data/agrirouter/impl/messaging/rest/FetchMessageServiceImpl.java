package com.dke.data.agrirouter.impl.messaging.rest;

import com.dke.data.agrirouter.api.cancellation.CancellationToken;
import com.dke.data.agrirouter.api.cancellation.DefaultCancellationToken;
import com.dke.data.agrirouter.api.dto.messaging.FetchMessageResponse;
import com.dke.data.agrirouter.api.dto.onboard.OnboardingResponse;
import com.dke.data.agrirouter.api.service.messaging.http.FetchMessageService;
import com.dke.data.agrirouter.api.service.parameters.FetchMessageParameters;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

public class FetchMessageServiceImpl implements FetchMessageService, MessageFetcher {

    @Override
    public Optional<List<FetchMessageResponse>> fetch(
            OnboardingResponse onboardingResponse, int maxTries, long interval) {
        var fetchMessageParameters = new FetchMessageParameters();
        fetchMessageParameters.setOnboardingResponse(onboardingResponse);
        return fetch(fetchMessageParameters, maxTries, interval);
    }

    @Override
    public Optional<List<FetchMessageResponse>> fetch(
            FetchMessageParameters fetchMessageParameters, int maxTries, long interval) {
        fetchMessageParameters.validate();
        var response =
                poll(fetchMessageParameters, new DefaultCancellationToken(maxTries, interval));
        return response.map(this::parseJson);
    }

    @Override
    public Optional<List<FetchMessageResponse>> fetch(
            OnboardingResponse onboardingResponse, CancellationToken cancellationToken) {
        var fetchMessageParameters = new FetchMessageParameters();
        fetchMessageParameters.setOnboardingResponse(onboardingResponse);
        return fetch(fetchMessageParameters, cancellationToken);
    }

    @Override
    public Optional<List<FetchMessageResponse>> fetch(
            FetchMessageParameters fetchMessageParameters, CancellationToken cancellationToken) {
        fetchMessageParameters.validate();
        var response = poll(fetchMessageParameters, cancellationToken);
        return response.map(this::parseJson);
    }

    private List<FetchMessageResponse> parseJson(String json) {
        var type = new TypeToken<List<FetchMessageResponse>>() {
        }.getType();
        return new Gson().fromJson(json, type);
    }

    @Override
    public FetchMessageResponse parseJson(byte[] json) {
        var type = new TypeToken<FetchMessageResponse>() {
        }.getType();
        return new Gson().fromJson(new String(json), type);
    }
}
