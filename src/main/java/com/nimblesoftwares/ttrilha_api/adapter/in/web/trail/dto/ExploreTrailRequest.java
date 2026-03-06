package com.nimblesoftwares.ttrilha_api.adapter.in.web.trail.dto;

import com.nimblesoftwares.ttrilha_api.adapter.in.web.trail.validator.ValidExploreRequest;
import com.nimblesoftwares.ttrilha_api.application.trail.command.ExploreTrailCommand;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

@ValidExploreRequest
public record ExploreTrailRequest(
    @DecimalMin(value = "-90.0", message = "Min latitude is -90")
    @DecimalMax(value = "90.0", message = "Max latitude is 90")
    BigDecimal lat,

    @DecimalMin(value = "-180.0", message = "Min longitude is -180")
    @DecimalMax(value = "180.0", message = "Max longitude is 180")
    BigDecimal  lon,

    @Size(min = 2, max = 100, message = "Location Name should be between 2 and 100 characters")
    String locationName,

    @Min(value = 1, message = "Min radius is 1km")
    @Max(value = 100, message = "Max radius is 100km")
    Integer radiusKm
) {

    public ExploreTrailCommand toCommand() {
        return new ExploreTrailCommand(lat, lon, locationName, radiusKm);
    }
}
