package com.dke.data.agrirouter.api.env;

/**
 * Abstraction of the QA environment, currently no overrides because the default is QA already.
 *
 * @deprecated This class is deprecated and will be removed in future versions.
 * The new agrirouter environment will be the default to use
 */
@Deprecated(since = "3.2.0", forRemoval = true)
public abstract class QA implements Environment {

    private static final String ENV_BASE_URL = "https://agrirouter-qa.cfapps.eu10.hana.ondemand.com";
    private static final String API_PREFIX = "/api/v1.0";
    private static final String REGISTRATION_SERVICE_URL =
            "https://agrirouter-registration-service-hubqa-eu10.cfapps.eu10.hana.ondemand.com";

    @Override
    public String getEnvironmentBaseUrl() {
        return ENV_BASE_URL;
    }

    @Override
    public String getApiPrefix() {
        return API_PREFIX;
    }

    @Override
    public String getRegistrationServiceUrl() {
        return REGISTRATION_SERVICE_URL;
    }
}
