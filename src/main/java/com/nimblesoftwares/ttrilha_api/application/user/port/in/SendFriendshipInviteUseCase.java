package com.nimblesoftwares.ttrilha_api.application.user.port.in;

import com.nimblesoftwares.ttrilha_api.application.user.command.SendFriendshipInviteCommand;

import java.util.UUID;

public interface SendFriendshipInviteUseCase {

  UUID execute(SendFriendshipInviteCommand command);
}
