package com.nimblesoftwares.ttrilha_api.adapter.in.web.trail.validator;

import com.nimblesoftwares.ttrilha_api.adapter.in.web.trail.dto.ExploreTrailRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ExploreRequestValidator implements ConstraintValidator<ValidExploreRequest, ExploreTrailRequest> {

  @Override
  public boolean isValid(ExploreTrailRequest request, ConstraintValidatorContext ctx) {

    boolean hasCoords = request.lat() != null && request.lon() != null;
    boolean hasLocation = request.locationName() != null && !request.locationName().isBlank();

    return hasCoords || hasLocation;
  }
}
