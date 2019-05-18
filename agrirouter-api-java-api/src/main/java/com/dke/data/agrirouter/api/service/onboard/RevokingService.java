package com.dke.data.agrirouter.api.service.onboard;

import com.dke.data.agrirouter.api.enums.RevokeResponse;
import com.dke.data.agrirouter.api.service.parameters.RevokeParameters;

public interface RevokingService {

  RevokeResponse revoke(RevokeParameters revokeParameters);
}
