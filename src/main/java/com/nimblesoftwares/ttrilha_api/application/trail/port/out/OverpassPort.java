package com.nimblesoftwares.ttrilha_api.application.trail.port.out;

import com.nimblesoftwares.ttrilha_api.domain.trail.model.BoundingBox;
import com.nimblesoftwares.ttrilha_api.domain.trail.model.Trail;

import java.util.List;

public interface OverpassPort {

  List<Trail> searchTrails(BoundingBox bbox);
}
