package com.dke.data.agrirouter.impl.messaging.rest;

import com.dke.data.agrirouter.api.dto.messaging.FetchMessageResponse;
import com.dke.data.agrirouter.api.dto.onboard.OnboardingResponse;
import com.dke.data.agrirouter.api.service.messaging.FetchMessageService;
import com.dke.data.agrirouter.api.service.parameters.FetchMessageParameters;
import com.dke.data.agrirouter.impl.validation.ResponseValidator;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

public class FetchMessageServiceImpl implements FetchMessageService, MessageFetcher, ResponseValidator {

    public Optional<List<FetchMessageResponse>> fetch(OnboardingResponse onboardingResponse, int maxTries, long interval) {
        FetchMessageParameters fetchMessageParameters = new FetchMessageParameters();
        fetchMessageParameters.setOnboardingResponse(onboardingResponse);
        return this.fetch(fetchMessageParameters, maxTries, interval);
    }

    public Optional<List<FetchMessageResponse>> fetch(FetchMessageParameters parameters, int maxTries, long interval) {
        parameters.validate();
        Optional<String> response = this.poll(parameters, maxTries, interval);
        return response.map(this::parseJson);
    }

    private List<FetchMessageResponse> parseJson(String json) {
        Type type = new TypeToken<List<FetchMessageResponse>>() {
        }.getType();
        return new Gson().fromJson(json, type);
    }
}
