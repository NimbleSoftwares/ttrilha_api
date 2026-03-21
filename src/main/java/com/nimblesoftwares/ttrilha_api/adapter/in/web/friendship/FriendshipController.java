package com.nimblesoftwares.ttrilha_api.adapter.in.web.friendship;

import com.nimblesoftwares.ttrilha_api.adapter.in.web.friendship.dto.BlockUserRequest;
import com.nimblesoftwares.ttrilha_api.adapter.in.web.friendship.dto.FriendResponse;
import com.nimblesoftwares.ttrilha_api.adapter.in.web.friendship.dto.FriendshipSolicitationResponse;
import com.nimblesoftwares.ttrilha_api.adapter.in.web.friendship.dto.PendingInviteResponse;
import com.nimblesoftwares.ttrilha_api.adapter.in.web.friendship.dto.RespondFriendshipInviteRequest;
import com.nimblesoftwares.ttrilha_api.adapter.in.web.friendship.dto.SendFriendshipInviteRequest;
import com.nimblesoftwares.ttrilha_api.adapter.in.web.shared.ProviderIdentity;
import com.nimblesoftwares.ttrilha_api.application.user.command.BlockUserCommand;
import com.nimblesoftwares.ttrilha_api.application.user.command.RespondFriendshipInviteCommand;
import com.nimblesoftwares.ttrilha_api.application.user.command.SendFriendshipInviteCommand;
import com.nimblesoftwares.ttrilha_api.application.user.command.UnblockUserCommand;
import com.nimblesoftwares.ttrilha_api.application.user.port.in.BlockUserUseCase;
import com.nimblesoftwares.ttrilha_api.application.user.port.in.GetUserIdBySubUseCase;
import com.nimblesoftwares.ttrilha_api.application.user.port.in.ListFriendsUseCase;
import com.nimblesoftwares.ttrilha_api.application.user.port.in.ListPendingInvitesUseCase;
import com.nimblesoftwares.ttrilha_api.application.user.port.in.RespondFriendshipInviteUseCase;
import com.nimblesoftwares.ttrilha_api.application.user.port.in.SendFriendshipInviteUseCase;
import com.nimblesoftwares.ttrilha_api.application.user.port.in.UnblockUserUseCase;
import com.nimblesoftwares.ttrilha_api.domain.user.model.Friendship;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Friendship", description = "Friendship Operations")
@RestController
@RequestMapping("/api/v1/friendships")
public class FriendshipController {

  private final GetUserIdBySubUseCase getUserIdBySubUseCase;
  private final SendFriendshipInviteUseCase sendFriendshipInviteUseCase;
  private final RespondFriendshipInviteUseCase respondFriendshipInviteUseCase;
  private final ListFriendsUseCase listFriendsUseCase;
  private final ListPendingInvitesUseCase listPendingInvitesUseCase;
  private final BlockUserUseCase blockUserUseCase;
  private final UnblockUserUseCase unblockUserUseCase;

  public FriendshipController(
      GetUserIdBySubUseCase getUserIdBySubUseCase,
      SendFriendshipInviteUseCase sendFriendshipInviteUseCase,
      RespondFriendshipInviteUseCase respondFriendshipInviteUseCase,
      ListFriendsUseCase listFriendsUseCase,
      ListPendingInvitesUseCase listPendingInvitesUseCase,
      BlockUserUseCase blockUserUseCase,
      UnblockUserUseCase unblockUserUseCase) {
    this.getUserIdBySubUseCase = getUserIdBySubUseCase;
    this.sendFriendshipInviteUseCase = sendFriendshipInviteUseCase;
    this.respondFriendshipInviteUseCase = respondFriendshipInviteUseCase;
    this.listFriendsUseCase = listFriendsUseCase;
    this.listPendingInvitesUseCase = listPendingInvitesUseCase;
    this.blockUserUseCase = blockUserUseCase;
    this.unblockUserUseCase = unblockUserUseCase;
  }

  @PostMapping(value = "/invite", consumes = "application/json", produces = "application/json")
  @Operation(
      summary = "Send friendship invitation",
      description = "Sends a friendship invitation to another user",
      security = @SecurityRequirement(name = "bearerAuth")
  )
  @ApiResponses({
      @ApiResponse(
          responseCode = "200",
          description = "Invitation sent successfully",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = FriendshipSolicitationResponse.class))
      ),
      @ApiResponse(responseCode = "400", description = "Bad Request"),
      @ApiResponse(responseCode = "401", description = "Unauthorized"),
      @ApiResponse(responseCode = "403", description = "Forbidden — a block exists between these users"),
      @ApiResponse(responseCode = "422", description = "Cannot send invite to yourself"),
      @ApiResponse(responseCode = "500", description = "Internal Server Error")
  })
  public ResponseEntity<FriendshipSolicitationResponse> sendInvite(
      @AuthenticationPrincipal Jwt jwt,
      @RequestBody @Valid SendFriendshipInviteRequest request) {

    UUID requesterId = resolveCurrentUserId(jwt);
    UUID solicitationId = sendFriendshipInviteUseCase.execute(new SendFriendshipInviteCommand(requesterId, request.addresseeId()));

    return ResponseEntity.ok(new FriendshipSolicitationResponse(solicitationId.toString()));
  }

  @PatchMapping(value = "/{solicitationId}/respond", consumes = "application/json")
  @Operation(
      summary = "Accept or reject a friendship invitation",
      description = "Responds to a received friendship invitation. Accepted status creates the friendship. Valid values: ACCEPTED, REJECTED",
      security = @SecurityRequirement(name = "bearerAuth")
  )
  @ApiResponses({
      @ApiResponse(responseCode = "204", description = "Response registered successfully"),
      @ApiResponse(responseCode = "400", description = "Bad Request"),
      @ApiResponse(responseCode = "401", description = "Unauthorized"),
      @ApiResponse(responseCode = "404", description = "Solicitation not found"),
      @ApiResponse(responseCode = "500", description = "Internal Server Error")
  })
  public ResponseEntity<Void> respondInvite(
      @AuthenticationPrincipal Jwt jwt,
      @PathVariable UUID solicitationId,
      @RequestBody @Valid RespondFriendshipInviteRequest request) {

    UUID responderId = resolveCurrentUserId(jwt);
    respondFriendshipInviteUseCase.execute(new RespondFriendshipInviteCommand(solicitationId, responderId, request.status()));

    return ResponseEntity.noContent().build();
  }

  @GetMapping(value = "/pending", produces = "application/json")
  @Operation(
      summary = "List pending friendship invitations",
      description = "Returns the list of pending friendship invitations received by the authenticated user",
      security = @SecurityRequirement(name = "bearerAuth")
  )
  @ApiResponses({
      @ApiResponse(
          responseCode = "200",
          description = "Pending invitations listed successfully",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = PendingInviteResponse.class))
      ),
      @ApiResponse(responseCode = "401", description = "Unauthorized"),
      @ApiResponse(responseCode = "500", description = "Internal Server Error")
  })
  public ResponseEntity<List<PendingInviteResponse>> listPendingInvites(@AuthenticationPrincipal Jwt jwt) {
    UUID currentUserId = resolveCurrentUserId(jwt);
    List<PendingInviteResponse> response = listPendingInvitesUseCase.execute(currentUserId)
        .stream()
        .map(PendingInviteResponse::fromDomain)
        .toList();
    return ResponseEntity.ok(response);
  }

  @GetMapping(produces = "application/json")
  @Operation(
      summary = "List friends",
      description = "Returns the list of friends of the authenticated user",
      security = @SecurityRequirement(name = "bearerAuth")
  )
  @ApiResponses({
      @ApiResponse(
          responseCode = "200",
          description = "Friends listed successfully",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = FriendResponse.class))
      ),
      @ApiResponse(responseCode = "401", description = "Unauthorized"),
      @ApiResponse(responseCode = "500", description = "Internal Server Error")
  })
  public ResponseEntity<List<FriendResponse>> listFriends(@AuthenticationPrincipal Jwt jwt) {
    UUID currentUserId = resolveCurrentUserId(jwt);
    List<Friendship> friends = listFriendsUseCase.execute(currentUserId);
    List<FriendResponse> response = friends.stream().map(FriendResponse::fromDomain).toList();
    return ResponseEntity.ok(response);
  }

  @PostMapping(value = "/block", consumes = "application/json")
  @Operation(
      summary = "Block a user",
      description = "Blocks a user. Cancels any pending friendship solicitations and removes any existing friendship between the two users.",
      security = @SecurityRequirement(name = "bearerAuth")
  )
  @ApiResponses({
      @ApiResponse(responseCode = "204", description = "User blocked successfully"),
      @ApiResponse(responseCode = "400", description = "Bad Request"),
      @ApiResponse(responseCode = "401", description = "Unauthorized"),
      @ApiResponse(responseCode = "409", description = "User already blocked"),
      @ApiResponse(responseCode = "422", description = "Cannot block yourself"),
      @ApiResponse(responseCode = "500", description = "Internal Server Error")
  })
  public ResponseEntity<Void> blockUser(
      @AuthenticationPrincipal Jwt jwt,
      @RequestBody @Valid BlockUserRequest request) {

    UUID blockerId = resolveCurrentUserId(jwt);
    blockUserUseCase.execute(new BlockUserCommand(blockerId, request.blockedId()));

    return ResponseEntity.noContent().build();
  }

  @DeleteMapping(value = "/block/{blockedId}")
  @Operation(
      summary = "Unblock a user",
      description = "Removes an existing block on a user.",
      security = @SecurityRequirement(name = "bearerAuth")
  )
  @ApiResponses({
      @ApiResponse(responseCode = "204", description = "User unblocked successfully"),
      @ApiResponse(responseCode = "401", description = "Unauthorized"),
      @ApiResponse(responseCode = "404", description = "Block not found"),
      @ApiResponse(responseCode = "500", description = "Internal Server Error")
  })
  public ResponseEntity<Void> unblockUser(
      @AuthenticationPrincipal Jwt jwt,
      @PathVariable UUID blockedId) {

    UUID blockerId = resolveCurrentUserId(jwt);
    unblockUserUseCase.execute(new UnblockUserCommand(blockerId, blockedId));

    return ResponseEntity.noContent().build();
  }

  private UUID resolveCurrentUserId(Jwt jwt) {
    ProviderIdentity pi = ProviderIdentity.fromSub(jwt.getSubject());
    return getUserIdBySubUseCase.execute(pi.provider(), pi.providerUserId());
  }
}

