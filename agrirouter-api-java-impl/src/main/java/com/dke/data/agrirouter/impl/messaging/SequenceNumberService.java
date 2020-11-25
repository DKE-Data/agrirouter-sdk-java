package com.dke.data.agrirouter.impl.messaging;

import com.dke.data.agrirouter.api.dto.onboard.OnboardingResponse;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Service to generate sequence numbers while sending messages to the agrirouter.
 * The sequence number generation is based on the ID of the endpoint, therefore a sequence number can be used multiple times for different endpoints.
 */
public class SequenceNumberService {

    private static final ConcurrentHashMap<String, Long> sequenceNumbersForEndpoints = new ConcurrentHashMap<>();

    /**
     * Generate the sequence number for the onboarding response.
     *
     * @param onboardingResponse -
     * @return 1 if this was the first call, 1+n for the n-th call.
     */
    public static synchronized long generateSequenceNumberForEndpoint(OnboardingResponse onboardingResponse) {
        sequenceNumbersForEndpoints.putIfAbsent(onboardingResponse.getSensorAlternateId(), 1L);
        Long currentSequenceNumber = sequenceNumbersForEndpoints.get(onboardingResponse.getSensorAlternateId());
        sequenceNumbersForEndpoints.put(onboardingResponse.getSensorAlternateId(), currentSequenceNumber + 1);
        return currentSequenceNumber;
    }

}
