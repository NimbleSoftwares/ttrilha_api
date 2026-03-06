package com.nimblesoftwares.ttrilha_api.application.trail.port.in;

import com.nimblesoftwares.ttrilha_api.application.trail.command.ExploreTrailCommand;
import com.nimblesoftwares.ttrilha_api.application.trail.dto.ExploreTrailResult;

public interface ExploreTrailsUsecase {

  ExploreTrailResult execute(ExploreTrailCommand command);
}
