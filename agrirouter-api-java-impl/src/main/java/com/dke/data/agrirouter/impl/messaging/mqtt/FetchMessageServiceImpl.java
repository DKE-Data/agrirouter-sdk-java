package com.dke.data.agrirouter.impl.messaging.mqtt;

import com.dke.data.agrirouter.api.dto.messaging.FetchMessageResponse;
import com.dke.data.agrirouter.api.dto.onboard.OnboardingResponse;
import com.dke.data.agrirouter.api.service.messaging.FetchMessageService;
import com.dke.data.agrirouter.api.service.parameters.FetchMessageParameters;
import com.dke.data.agrirouter.impl.messaging.rest.MessageFetcher;
import com.dke.data.agrirouter.impl.validation.ResponseValidator;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

public class FetchMessageServiceImpl {

   public FetchMessageResponse parse(String json) {
        Type type = new TypeToken<List<FetchMessageResponse>>() {
        }.getType();
        return new Gson().fromJson(json, type);
    }
}
