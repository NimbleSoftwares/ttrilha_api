package com.nimblesoftwares.ttrilha_api.application.user.port.in;

import com.nimblesoftwares.ttrilha_api.application.user.command.RespondFriendshipInviteCommand;

public interface RespondFriendshipInviteUseCase {

  void execute(RespondFriendshipInviteCommand command);
}
