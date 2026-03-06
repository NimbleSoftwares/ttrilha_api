package com.nimblesoftwares.ttrilha_api.adapter.out.trail.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OverpassResponse(
    List<OverpassElement> elements
) {}
