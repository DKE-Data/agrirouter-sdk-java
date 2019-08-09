package com.dke.data.agrirouter.api.service.onboard;

import com.dke.data.agrirouter.api.dto.revoke.RevokingError;
import com.dke.data.agrirouter.api.enums.RevokeResponse;
import com.dke.data.agrirouter.api.service.parameters.RevokeParameters;
import java.util.Optional;

public interface RevokingService {

  RevokeResponse revoke(RevokeParameters revokeParameters);

  String getLastErrorAsString();

  Optional<RevokingError> getLastError();
}
