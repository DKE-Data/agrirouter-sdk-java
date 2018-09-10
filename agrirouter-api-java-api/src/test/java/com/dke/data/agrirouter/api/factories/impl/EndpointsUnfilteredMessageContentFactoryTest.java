package com.dke.data.agrirouter.api.factories.impl;

import agrirouter.request.payload.account.Endpoints;
import com.dke.data.agrirouter.api.enums.TechnicalMessageType;
import com.dke.data.agrirouter.api.service.parameters.EndpointsUnfilteredMessageParameters;
import com.google.protobuf.ByteString;
import kotlin.UninitializedPropertyAccessException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EndpointsUnfilteredMessageContentFactoryTest extends AbstractMessageContentFactoryTest<EndpointsUnfiltererdMessageContentFactory> {

    @Test
    void givenValidEndpointsUnfilteredMessageParameters_Message_ShouldNotFail() {
        EndpointsUnfilteredMessageParameters endpointsUnfilteredMessageParameters = new EndpointsUnfilteredMessageParameters();
        endpointsUnfilteredMessageParameters.direction = Endpoints.ListEndpointsQuery.Direction.SEND;
        endpointsUnfilteredMessageParameters.technicalMessageType = TechnicalMessageType.ISO_11783_TASKDATA_ZIP;
        ByteString message = this.getInstanceToTest().message(endpointsUnfilteredMessageParameters);
        assertFalse(message.isEmpty());
    }

    @Test
    void givenEmptyEndpointsUnfilteredMessageParameters_Message_ShouldThrowException() {
        EndpointsUnfilteredMessageParameters endpointsUnfilteredMessageParameters = new EndpointsUnfilteredMessageParameters();
        assertThrows(UninitializedPropertyAccessException.class, () -> this.getInstanceToTest().message(endpointsUnfilteredMessageParameters));
    }

    @Test
    @SuppressWarnings("ConstantConditions")
    void givenEndpointsUnfilteredMessageParametersWithNullValues_Message_ShouldThrowException() {
        EndpointsUnfilteredMessageParameters endpointsUnfilteredMessageParameters = new EndpointsUnfilteredMessageParameters();
        endpointsUnfilteredMessageParameters.direction = null;
        endpointsUnfilteredMessageParameters.technicalMessageType = null;
        assertThrows(UninitializedPropertyAccessException.class, () -> this.getInstanceToTest().message(endpointsUnfilteredMessageParameters));
    }

    @Override
    protected EndpointsUnfiltererdMessageContentFactory getInstanceToTest() {
        return new EndpointsUnfiltererdMessageContentFactory();
    }
}
