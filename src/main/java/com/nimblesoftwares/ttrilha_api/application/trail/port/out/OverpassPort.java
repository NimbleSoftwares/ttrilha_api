package com.nimblesoftwares.ttrilha_api.application.trail.port.out;

import com.nimblesoftwares.ttrilha_api.application.trail.dto.TrailData;
import com.nimblesoftwares.ttrilha_api.domain.trail.model.BoundingBox;

import java.util.List;

public interface OverpassPort {

  List<TrailData> searchTrails(BoundingBox bbox);
}
