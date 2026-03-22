package com.nimblesoftwares.ttrilha_api.config;

import com.nimblesoftwares.ttrilha_api.adapter.out.persistence.trail.mapper.LineStringMapper;
import com.nimblesoftwares.ttrilha_api.adapter.out.trail.GeocodingClient;
import com.nimblesoftwares.ttrilha_api.adapter.out.trail.OverpassClient;
import com.nimblesoftwares.ttrilha_api.application.trail.port.in.SaveTrailUseCase;
import com.nimblesoftwares.ttrilha_api.application.trail.port.out.GeocodingPort;
import com.nimblesoftwares.ttrilha_api.application.trail.port.out.OverpassPort;
import com.nimblesoftwares.ttrilha_api.application.trail.port.out.TrailRepositoryPort;
import com.nimblesoftwares.ttrilha_api.application.trail.service.ExploreTrailsService;
import com.nimblesoftwares.ttrilha_api.application.trail.service.SaveTrailFromOverpassService;
import com.nimblesoftwares.ttrilha_api.application.user.port.in.BlockUserUseCase;
import com.nimblesoftwares.ttrilha_api.application.user.port.in.GetUserByUsernameUseCase;
import com.nimblesoftwares.ttrilha_api.application.user.port.in.GetUserIdBySubUseCase;
import com.nimblesoftwares.ttrilha_api.application.user.port.in.ListFriendsUseCase;
import com.nimblesoftwares.ttrilha_api.application.user.port.in.ListPendingInvitesUseCase;
import com.nimblesoftwares.ttrilha_api.application.user.port.in.RespondFriendshipInviteUseCase;
import com.nimblesoftwares.ttrilha_api.application.user.port.in.SendFriendshipInviteUseCase;
import com.nimblesoftwares.ttrilha_api.application.user.port.in.UnblockUserUseCase;
import com.nimblesoftwares.ttrilha_api.application.user.port.out.FriendshipRepositoryPort;
import com.nimblesoftwares.ttrilha_api.application.user.port.out.FriendshipSolicitationRepositoryPort;
import com.nimblesoftwares.ttrilha_api.application.user.port.out.UserBlockRepositoryPort;
import com.nimblesoftwares.ttrilha_api.application.user.port.out.UserIdentityRepositoryPort;
import com.nimblesoftwares.ttrilha_api.application.user.port.out.UserRepositoryPort;
import com.nimblesoftwares.ttrilha_api.application.user.service.BlockUserService;
import com.nimblesoftwares.ttrilha_api.application.user.service.GetUserIdBySubService;
import com.nimblesoftwares.ttrilha_api.application.user.service.GetUserByUsernameService;
import com.nimblesoftwares.ttrilha_api.application.user.service.ListFriendsService;
import com.nimblesoftwares.ttrilha_api.application.user.service.ListPendingInvitesService;
import com.nimblesoftwares.ttrilha_api.application.user.service.RespondFriendshipInviteService;
import com.nimblesoftwares.ttrilha_api.application.user.service.SaveUserService;
import com.nimblesoftwares.ttrilha_api.application.user.service.SendFriendshipInviteService;
import com.nimblesoftwares.ttrilha_api.application.user.service.UnblockUserService;
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
  public OverpassPort overpassPort(@Value("${overpass.baseUrl}") String baseUrl) {
    return new OverpassClient(baseUrl);
  }

  @Bean
  public LineStringMapper lineStringMapper() {
    return new LineStringMapper();
  }

  @Bean
  public ExploreTrailsService exploreTrailsService(
      GeocodingPort geocodingPort,
      OverpassPort overpassPort) {
    return new ExploreTrailsService(overpassPort, geocodingPort);
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
}
