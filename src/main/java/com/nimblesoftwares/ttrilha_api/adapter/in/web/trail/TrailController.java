package com.nimblesoftwares.ttrilha_api.adapter.in.web.trail;

import com.nimblesoftwares.ttrilha_api.adapter.in.web.trail.dto.CreateTrailRequest;
import com.nimblesoftwares.ttrilha_api.adapter.in.web.trail.dto.ExploreTrailRequest;
import com.nimblesoftwares.ttrilha_api.adapter.out.trail.dto.OverpassResponse;
import com.nimblesoftwares.ttrilha_api.application.trail.port.in.ExploreTrailsUsecase;
import com.nimblesoftwares.ttrilha_api.application.trail.port.in.SaveTrailUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/trails")
public class TrailController {

  private final ExploreTrailsUsecase exploreTrailsUsecase;
  private final SaveTrailUseCase saveTrailUseCase;

  public TrailController(ExploreTrailsUsecase exploreTrailsUsecase,
                         SaveTrailUseCase saveTrailUseCase) {
    this.exploreTrailsUsecase = exploreTrailsUsecase;
    this.saveTrailUseCase = saveTrailUseCase;
  }

  @PostMapping("/explore")
  public ResponseEntity<OverpassResponse> explore(@Valid @RequestBody ExploreTrailRequest request) {

    OverpassResponse result = exploreTrailsUsecase.execute(request.toCommand());

    //ExploreTrailResponse response = ExploreTrailResponse.fromResult(result);

    return ResponseEntity.ok().body(result);
  }

  @PostMapping
  public ResponseEntity<UUID> create(
      @RequestBody @Valid CreateTrailRequest request) {

    UUID trailId = saveTrailUseCase.execute(request.toCommand());

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(trailId);
  }
}
