package com.nimblesoftwares.ttrilha_api.application.trail.port.in;

import com.nimblesoftwares.ttrilha_api.application.trail.command.SaveTrailCommand;

import java.util.UUID;

public interface SaveTrailUseCase {

  UUID execute(SaveTrailCommand command);
}
