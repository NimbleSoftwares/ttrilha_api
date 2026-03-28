package com.nimblesoftwares.ttrilha_api.config;

import com.nimblesoftwares.ttrilha_api.adapter.out.persistence.trail.mapper.LineStringMapper;
import com.nimblesoftwares.ttrilha_api.adapter.out.persistence.trail.mapper.OverpassMapper;
import com.nimblesoftwares.ttrilha_api.adapter.out.trail.GeocodingClient;
import com.nimblesoftwares.ttrilha_api.adapter.out.trail.OverpassClient;
import com.nimblesoftwares.ttrilha_api.application.expedition.port.in.*;
import com.nimblesoftwares.ttrilha_api.application.expedition.port.out.ExpeditionRepositoryPort;
import com.nimblesoftwares.ttrilha_api.application.expedition.service.*;
import com.nimblesoftwares.ttrilha_api.application.trail.port.in.SaveTrailUseCase;
import com.nimblesoftwares.ttrilha_api.application.trail.port.out.GeocodingPort;
import com.nimblesoftwares.ttrilha_api.application.trail.port.out.OverpassPort;
import com.nimblesoftwares.ttrilha_api.application.trail.port.out.TrailRepositoryPort;
import com.nimblesoftwares.ttrilha_api.application.trail.service.ExploreTrailsService;
import com.nimblesoftwares.ttrilha_api.application.trail.service.SaveTrailFromOverpassService;
import com.nimblesoftwares.ttrilha_api.application.user.port.in.*;
import com.nimblesoftwares.ttrilha_api.application.user.port.out.*;
import com.nimblesoftwares.ttrilha_api.application.user.service.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

  @Bean
  public GeocodingPort geocodingPort() {
    return new GeocodingClient();
  }

  @Bean
  public OverpassPort overpassPort(@Value("${overpass.baseUrl}") String baseUrl, OverpassMapper overpassMapper) {
    return new OverpassClient(baseUrl, overpassMapper);
  }

  @Bean
  public LineStringMapper lineStringMapper() {
    return new LineStringMapper();
  }

  @Bean
  public ExploreTrailsService exploreTrailsService(
      GeocodingPort geocodingPort,
      OverpassPort overpassPort,
      TrailRepositoryPort trailRepositoryPort) {
    return new ExploreTrailsService(overpassPort, geocodingPort, trailRepositoryPort);
  }

  @Bean
  public SaveTrailUseCase saveTrailUseCase(
      TrailRepositoryPort trailRepositoryPort,
      LineStringMapper lineStringMapper) {
    return new SaveTrailFromOverpassService(lineStringMapper, trailRepositoryPort);
  }

  @Bean
  public SaveUserService saveUserService(
      UserRepositoryPort userRepositoryPort,
      UserIdentityRepositoryPort userIdentityRepository
  ) {
    return new SaveUserService(userIdentityRepository, userRepositoryPort);
  }

  @Bean
  public GetUserIdBySubUseCase getUserIdBySubUseCase(UserIdentityRepositoryPort userIdentityRepository) {
    return new GetUserIdBySubService(userIdentityRepository);
  }

  @Bean
  public GetUserByUsernameUseCase getUserByUsernameUseCase(UserRepositoryPort userRepositoryPort) {
    return new GetUserByUsernameService(userRepositoryPort);
  }

  @Bean
  public SendFriendshipInviteUseCase sendFriendshipInviteUseCase(
      FriendshipSolicitationRepositoryPort solicitationRepository,
      UserBlockRepositoryPort userBlockRepository) {
    return new SendFriendshipInviteService(solicitationRepository, userBlockRepository);
  }

  @Bean
  public RespondFriendshipInviteUseCase respondFriendshipInviteUseCase(
      FriendshipSolicitationRepositoryPort solicitationRepository,
      FriendshipRepositoryPort friendshipRepository) {
    return new RespondFriendshipInviteService(solicitationRepository, friendshipRepository);
  }

  @Bean
  public ListFriendsUseCase listFriendsUseCase(
      FriendshipRepositoryPort friendshipRepository,
      UserRepositoryPort userRepositoryPort) {
    return new ListFriendsService(friendshipRepository, userRepositoryPort);
  }

  @Bean
  public ListPendingInvitesUseCase listPendingInvitesUseCase(
      FriendshipSolicitationRepositoryPort solicitationRepository,
      UserRepositoryPort userRepositoryPort) {
    return new ListPendingInvitesService(solicitationRepository, userRepositoryPort);
  }

  @Bean
  public BlockUserUseCase blockUserUseCase(
      UserBlockRepositoryPort userBlockRepository,
      FriendshipRepositoryPort friendshipRepository,
      FriendshipSolicitationRepositoryPort solicitationRepository) {
    return new BlockUserService(userBlockRepository, friendshipRepository, solicitationRepository);
  }

  @Bean
  public UnblockUserUseCase unblockUserUseCase(UserBlockRepositoryPort userBlockRepository) {
    return new UnblockUserService(userBlockRepository);
  }

  @Bean
  public CreateExpeditionUseCase createExpeditionUseCase(
      SaveTrailUseCase saveTrailUseCase,
      ExpeditionRepositoryPort expeditionRepository) {
    return new CreateExpeditionService(saveTrailUseCase, expeditionRepository);
  }

  @Bean
  public GetExpeditionUseCase getExpeditionUseCase(
      ExpeditionRepositoryPort expeditionRepository,
      TrailRepositoryPort trailRepository,
      UserRepositoryPort userRepositoryPort,
      LineStringMapper lineStringMapper) {
    return new GetExpeditionService(expeditionRepository, trailRepository, userRepositoryPort, lineStringMapper);
  }

  @Bean
  public ListUserExpeditionsUseCase listUserExpeditionsUseCase(
      ExpeditionRepositoryPort expeditionRepository,
      TrailRepositoryPort trailRepository,
      UserRepositoryPort userRepositoryPort) {
    return new ListUserExpeditionsService(expeditionRepository, trailRepository, userRepositoryPort);
  }

  @Bean
  public InviteToExpeditionUseCase inviteToExpeditionUseCase(
      ExpeditionRepositoryPort expeditionRepository,
      FriendshipRepositoryPort friendshipRepository) {
    return new InviteToExpeditionService(expeditionRepository, friendshipRepository);
  }

  @Bean
  public RespondExpeditionInviteUseCase respondExpeditionInviteUseCase(ExpeditionRepositoryPort expeditionRepository) {
    return new RespondExpeditionInviteService(expeditionRepository);
  }

  @Bean
  public ListPendingExpeditionInvitesUseCase listPendingExpeditionInvitesUseCase(
      ExpeditionRepositoryPort expeditionRepository,
      UserRepositoryPort userRepositoryPort) {
    return new ListPendingExpeditionInvitesService(expeditionRepository, userRepositoryPort);
  }

  @Bean
  public UpdateExpeditionUseCase updateExpeditionUseCase(
      ExpeditionRepositoryPort expeditionRepository,
      SaveTrailUseCase saveTrailUseCase) {
    return new UpdateExpeditionService(expeditionRepository, saveTrailUseCase);
  }

  @Bean
  public DeleteExpeditionUseCase deleteExpeditionUseCase(ExpeditionRepositoryPort expeditionRepository) {
    return new DeleteExpeditionService(expeditionRepository);
  }
}
