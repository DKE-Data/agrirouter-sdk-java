package com.dke.data.agrirouter.impl.messaging.rest;

import com.dke.data.agrirouter.api.env.Environment;
import com.dke.data.agrirouter.api.messaging.HttpAsyncMessageSendingResult;
import com.dke.data.agrirouter.api.service.messaging.encoding.EncodeMessageService;
import com.dke.data.agrirouter.api.service.messaging.http.SetCapabilityService;
import com.dke.data.agrirouter.api.service.parameters.SendMessageParameters;
import com.dke.data.agrirouter.api.service.parameters.SetCapabilitiesParameters;
import com.dke.data.agrirouter.impl.messaging.MessageEncoder;
import com.dke.data.agrirouter.impl.messaging.encoding.EncodeMessageServiceImpl;
import com.dke.data.agrirouter.impl.validation.ResponseValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;

public class SetCapabilityServiceImpl
        implements SetCapabilityService, MessageSender, MessageEncoder, ResponseValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(SetCapabilityServiceImpl.class);

    private final EncodeMessageService encodeMessageService;

    /**
     * Implements the SetCapabilityService interface and provides methods for sending
     * capability data to the agrirouter.
     *
     * @deprecated This class is deprecated since version 3.1.0 and will be removed in a future release.
     * The environment parameter is no longer necessary to use the service.
     * Please use the default constructor instead.
     */
    @Deprecated(since = "3.1.0", forRemoval = true)
    public SetCapabilityServiceImpl(Environment ignoredEnvironment) {
        LOGGER.warn("This constructor is deprecated. The environment is not necessary to use the service. Within the next major release this constructor will be removed.");
        this.encodeMessageService = new EncodeMessageServiceImpl();
    }

    public SetCapabilityServiceImpl() {
        this.encodeMessageService = new EncodeMessageServiceImpl();
    }

    @Override
    public String send(SetCapabilitiesParameters parameters) {
        parameters.validate();
        var encodedMessage = this.encode(parameters);
        var sendMessageParameters = new SendMessageParameters();
        sendMessageParameters.setOnboardingResponse(parameters.getOnboardingResponse());
        sendMessageParameters.setEncodedMessages(
                Collections.singletonList(encodedMessage.getEncodedMessage()));
        var response = this.sendMessage(sendMessageParameters);
        this.assertStatusCodeIsOk(response.getNativeResponse().getStatus());
        return encodedMessage.getApplicationMessageID();
    }

    @Override
    public HttpAsyncMessageSendingResult sendAsync(SetCapabilitiesParameters parameters) {
        parameters.validate();
        var encodedMessage = this.encode(parameters);
        var sendMessageParameters = new SendMessageParameters();
        sendMessageParameters.setOnboardingResponse(parameters.getOnboardingResponse());
        sendMessageParameters.setEncodedMessages(
                Collections.singletonList(encodedMessage.getEncodedMessage()));
        return new HttpAsyncMessageSendingResult(
                this.sendMessageAsync(sendMessageParameters), encodedMessage.getApplicationMessageID());
    }

    @Override
    public EncodeMessageService getEncodeMessageService() {
        return encodeMessageService;
    }
}
