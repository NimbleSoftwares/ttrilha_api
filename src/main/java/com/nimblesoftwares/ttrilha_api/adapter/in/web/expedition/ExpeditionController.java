package com.nimblesoftwares.ttrilha_api.adapter.in.web.expedition;

import com.nimblesoftwares.ttrilha_api.adapter.in.web.expedition.dto.*;
import com.nimblesoftwares.ttrilha_api.adapter.in.web.shared.ProviderIdentity;
import com.nimblesoftwares.ttrilha_api.application.expedition.command.InviteToExpeditionCommand;
import com.nimblesoftwares.ttrilha_api.adapter.in.web.expedition.dto.RespondExpeditionInviteRequest;
import com.nimblesoftwares.ttrilha_api.application.expedition.command.RespondExpeditionInviteCommand;
import com.nimblesoftwares.ttrilha_api.application.expedition.port.in.*;
import com.nimblesoftwares.ttrilha_api.application.user.port.in.GetUserIdBySubUseCase;
import com.nimblesoftwares.ttrilha_api.domain.expedition.model.ExpeditionStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Tag(name = "Expedition", description = "Expedition Operations")
@RestController
@RequestMapping("/api/v1/expeditions")
public class ExpeditionController {

  private final CreateExpeditionUseCase createExpeditionUseCase;
  private final GetExpeditionUseCase getExpeditionUseCase;
  private final ListUserExpeditionsUseCase listUserExpeditionsUseCase;
  private final InviteToExpeditionUseCase inviteToExpeditionUseCase;
  private final RespondExpeditionInviteUseCase respondExpeditionInviteUseCase;
  private final ListPendingExpeditionInvitesUseCase listPendingExpeditionInvitesUseCase;
  private final UpdateExpeditionUseCase updateExpeditionUseCase;
  private final DeleteExpeditionUseCase deleteExpeditionUseCase;
  private final GetUserIdBySubUseCase getUserIdBySubUseCase;

  public ExpeditionController(
      CreateExpeditionUseCase createExpeditionUseCase,
      GetExpeditionUseCase getExpeditionUseCase,
      ListUserExpeditionsUseCase listUserExpeditionsUseCase,
      InviteToExpeditionUseCase inviteToExpeditionUseCase,
      RespondExpeditionInviteUseCase respondExpeditionInviteUseCase,
      ListPendingExpeditionInvitesUseCase listPendingExpeditionInvitesUseCase,
      UpdateExpeditionUseCase updateExpeditionUseCase,
      DeleteExpeditionUseCase deleteExpeditionUseCase,
      GetUserIdBySubUseCase getUserIdBySubUseCase) {
    this.createExpeditionUseCase = createExpeditionUseCase;
    this.getExpeditionUseCase = getExpeditionUseCase;
    this.listUserExpeditionsUseCase = listUserExpeditionsUseCase;
    this.inviteToExpeditionUseCase = inviteToExpeditionUseCase;
    this.respondExpeditionInviteUseCase = respondExpeditionInviteUseCase;
    this.listPendingExpeditionInvitesUseCase = listPendingExpeditionInvitesUseCase;
    this.updateExpeditionUseCase = updateExpeditionUseCase;
    this.deleteExpeditionUseCase = deleteExpeditionUseCase;
    this.getUserIdBySubUseCase = getUserIdBySubUseCase;
  }

  // ─── CREATE ────────────────────────────────────────────────────────────────

  @PostMapping(consumes = "application/json", produces = "application/json")
  @Operation(summary = "Create an expedition", security = @SecurityRequirement(name = "bearerAuth"))
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "Created",
          content = @Content(schema = @Schema(implementation = CreateExpeditionResponse.class))),
      @ApiResponse(responseCode = "400", description = "Bad Request"),
      @ApiResponse(responseCode = "401", description = "Unauthorized")
  })
  public ResponseEntity<CreateExpeditionResponse> create(
      @AuthenticationPrincipal Jwt jwt,
      @RequestBody @Valid CreateExpeditionRequest request) {

    UUID createdByUserId = resolveCurrentUserId(jwt);
    UUID expeditionId = createExpeditionUseCase.execute(request.toCommand(createdByUserId));
    return ResponseEntity.status(HttpStatus.CREATED).body(new CreateExpeditionResponse(expeditionId));
  }

  // ─── LIST (unified: optional status filter + sort) ─────────────────────────

  @GetMapping(produces = "application/json")
  @Operation(
      summary = "List user expeditions",
      description = "Returns expeditions where the user is an ACCEPTED member. Optional `status` filter. Without status, sorted by startDate; with status, sorted by endDate DESC by default. Use `sort=ASC|DESC` to override.",
      security = @SecurityRequirement(name = "bearerAuth"))
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "OK",
          content = @Content(schema = @Schema(implementation = ExpeditionSummaryResponse.class))),
      @ApiResponse(responseCode = "401", description = "Unauthorized")
  })
  public ResponseEntity<List<ExpeditionSummaryResponse>> listMyExpeditions(
      @AuthenticationPrincipal Jwt jwt,
      @RequestParam(required = false) ExpeditionStatus status,
      @RequestParam(defaultValue = "ASC") String sort) {

    UUID currentUserId = resolveCurrentUserId(jwt);
    // When filtering by status default ordering is DESC (most recently ended first)
    String effectiveSort = (status != null && sort.equals("ASC")) ? "DESC" : sort;

    List<ExpeditionSummaryResponse> response = listUserExpeditionsUseCase
        .execute(currentUserId, Optional.ofNullable(status), effectiveSort)
        .stream()
        .map(ExpeditionSummaryResponse::fromResult)
        .toList();

    return ResponseEntity.ok(response);
  }

  // ─── GET DETAIL ────────────────────────────────────────────────────────────

  @GetMapping(value = "/{id}", produces = "application/json")
  @Operation(summary = "Get expedition detail", security = @SecurityRequirement(name = "bearerAuth"))
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "OK",
          content = @Content(schema = @Schema(implementation = ExpeditionDetailResponse.class))),
      @ApiResponse(responseCode = "401", description = "Unauthorized"),
      @ApiResponse(responseCode = "404", description = "Not Found")
  })
  public ResponseEntity<ExpeditionDetailResponse> getExpedition(
      @AuthenticationPrincipal Jwt jwt,
      @PathVariable UUID id) {

    UUID currentUserId = resolveCurrentUserId(jwt);
    return ResponseEntity.ok(
        ExpeditionDetailResponse.fromResult(getExpeditionUseCase.execute(id, currentUserId)));
  }

  // ─── UPDATE ────────────────────────────────────────────────────────────────

  @PutMapping(value = "/{id}", consumes = "application/json")
  @Operation(summary = "Update an expedition (owner only)", security = @SecurityRequirement(name = "bearerAuth"))
  @ApiResponses({
      @ApiResponse(responseCode = "204", description = "Updated"),
      @ApiResponse(responseCode = "400", description = "Bad Request"),
      @ApiResponse(responseCode = "401", description = "Unauthorized"),
      @ApiResponse(responseCode = "403", description = "Forbidden"),
      @ApiResponse(responseCode = "404", description = "Not Found")
  })
  public ResponseEntity<Void> updateExpedition(
      @AuthenticationPrincipal Jwt jwt,
      @PathVariable UUID id,
      @RequestBody UpdateExpeditionRequest request) {

    UUID currentUserId = resolveCurrentUserId(jwt);
    updateExpeditionUseCase.execute(request.toCommand(id, currentUserId));
    return ResponseEntity.noContent().build();
  }

  // ─── DELETE (soft) ─────────────────────────────────────────────────────────

  @DeleteMapping("/{id}")
  @Operation(
      summary = "Delete an expedition (owner only)",
      description = "Owner is removed from the member list and expedition status is set to CANCELLED. Other members continue to see the expedition.",
      security = @SecurityRequirement(name = "bearerAuth"))
  @ApiResponses({
      @ApiResponse(responseCode = "204", description = "Deleted"),
      @ApiResponse(responseCode = "401", description = "Unauthorized"),
      @ApiResponse(responseCode = "403", description = "Forbidden"),
      @ApiResponse(responseCode = "404", description = "Not Found")
  })
  public ResponseEntity<Void> deleteExpedition(
      @AuthenticationPrincipal Jwt jwt,
      @PathVariable UUID id) {

    UUID currentUserId = resolveCurrentUserId(jwt);
    deleteExpeditionUseCase.execute(id, currentUserId);
    return ResponseEntity.noContent().build();
  }

  // ─── INVITES ───────────────────────────────────────────────────────────────

  @PostMapping(value = "/{id}/invites", consumes = "application/json")
  @Operation(
      summary = "Invite a friend to an expedition",
      description = "Invitee must be a friend of the inviter. Max 3 invite attempts per (expedition, user).",
      security = @SecurityRequirement(name = "bearerAuth"))
  @ApiResponses({
      @ApiResponse(responseCode = "204", description = "Invite sent"),
      @ApiResponse(responseCode = "400", description = "Bad Request"),
      @ApiResponse(responseCode = "401", description = "Unauthorized"),
      @ApiResponse(responseCode = "403", description = "Forbidden — not a member, or invitee is not a friend"),
      @ApiResponse(responseCode = "404", description = "Expedition not found"),
      @ApiResponse(responseCode = "409", description = "Already a member / pending, or max invites reached")
  })
  public ResponseEntity<Void> invite(
      @AuthenticationPrincipal Jwt jwt,
      @PathVariable UUID id,
      @RequestBody @Valid ExpeditionInviteRequest request) {

    UUID inviterId = resolveCurrentUserId(jwt);
    inviteToExpeditionUseCase.execute(new InviteToExpeditionCommand(id, inviterId, request.inviteeId()));
    return ResponseEntity.noContent().build();
  }

  @PatchMapping(value = "/invites/{inviteId}/respond", consumes = "application/json")
  @Operation(
      summary = "Respond to an expedition invite",
      description = "Accept or reject a pending expedition invite. Send `{ \"status\": \"ACCEPTED\" }` or `{ \"status\": \"REJECTED\" }`.",
      security = @SecurityRequirement(name = "bearerAuth"))
  @ApiResponses({
      @ApiResponse(responseCode = "204", description = "Response registered"),
      @ApiResponse(responseCode = "400", description = "Bad Request — invalid status value"),
      @ApiResponse(responseCode = "401", description = "Unauthorized"),
      @ApiResponse(responseCode = "403", description = "Forbidden — not your invite, or invite no longer pending"),
      @ApiResponse(responseCode = "404", description = "Invite not found")
  })
  public ResponseEntity<Void> respondInvite(
      @AuthenticationPrincipal Jwt jwt,
      @PathVariable UUID inviteId,
      @RequestBody @Valid RespondExpeditionInviteRequest request) {

    UUID responderId = resolveCurrentUserId(jwt);
    respondExpeditionInviteUseCase.execute(
        new RespondExpeditionInviteCommand(inviteId, responderId, request.status()));
    return ResponseEntity.noContent().build();
  }

  @GetMapping(value = "/invites/pending", produces = "application/json")
  @Operation(summary = "List pending expedition invites for the logged-in user", security = @SecurityRequirement(name = "bearerAuth"))
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "OK",
          content = @Content(schema = @Schema(implementation = PendingExpeditionInviteResponse.class))),
      @ApiResponse(responseCode = "401", description = "Unauthorized")
  })
  public ResponseEntity<List<PendingExpeditionInviteResponse>> listPendingInvites(
      @AuthenticationPrincipal Jwt jwt) {

    UUID currentUserId = resolveCurrentUserId(jwt);
    List<PendingExpeditionInviteResponse> response = listPendingExpeditionInvitesUseCase
        .execute(currentUserId)
        .stream()
        .map(PendingExpeditionInviteResponse::fromResult)
        .toList();
    return ResponseEntity.ok(response);
  }

  // ─── HELPER ────────────────────────────────────────────────────────────────

  private UUID resolveCurrentUserId(Jwt jwt) {
    ProviderIdentity pi = ProviderIdentity.fromSub(jwt.getSubject());
    return getUserIdBySubUseCase.execute(pi.provider(), pi.providerUserId());
  }
}

