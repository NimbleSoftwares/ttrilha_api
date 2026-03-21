package com.nimblesoftwares.ttrilha_api.application.user.port.in;

import com.nimblesoftwares.ttrilha_api.domain.user.model.ProviderEnum;

import java.util.UUID;

public interface GetUserIdBySubUseCase {

  UUID execute(ProviderEnum provider, String providerUserId);
}
