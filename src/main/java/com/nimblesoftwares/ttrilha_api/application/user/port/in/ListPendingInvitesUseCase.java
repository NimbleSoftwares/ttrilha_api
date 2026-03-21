package com.nimblesoftwares.ttrilha_api.application.user.port.in;

import com.nimblesoftwares.ttrilha_api.application.user.dto.PendingInviteResult;

import java.util.List;
import java.util.UUID;

public interface ListPendingInvitesUseCase {

  List<PendingInviteResult> execute(UUID addresseeId);
}
