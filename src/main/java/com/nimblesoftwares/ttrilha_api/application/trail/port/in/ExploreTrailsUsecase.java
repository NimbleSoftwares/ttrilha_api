package com.nimblesoftwares.ttrilha_api.application.trail.port.in;

import com.nimblesoftwares.ttrilha_api.adapter.out.trail.dto.OverpassResponse;
import com.nimblesoftwares.ttrilha_api.application.trail.command.ExploreTrailCommand;

public interface ExploreTrailsUsecase {

  OverpassResponse execute(ExploreTrailCommand command);
}
