package com.nimblesoftwares.ttrilha_api.adapter.in.web.shared;

import com.nimblesoftwares.ttrilha_api.adapter.in.web.user.service.SaveUserService;
import com.nimblesoftwares.ttrilha_api.application.user.command.SaveUserCommand;
import com.nimblesoftwares.ttrilha_api.application.user.port.out.UserIdentityRepositoryPort;
import com.nimblesoftwares.ttrilha_api.domain.user.model.UserIdentity;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
public class CreateUserIfNotExistsFilter extends OncePerRequestFilter {

  private final SaveUserService saveUserService;
  private final UserIdentityRepositoryPort userIdentityRepository;

  public CreateUserIfNotExistsFilter(SaveUserService saveUserService, UserIdentityRepositoryPort userIdentityRepository) {
    this.saveUserService = saveUserService;
    this.userIdentityRepository = userIdentityRepository;
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    return request.getRequestURI().equals("/api/v1/users/sync");
  }

  @Override
  protected void doFilterInternal(@NonNull HttpServletRequest request,
                                  @NonNull HttpServletResponse response,
                                  @NonNull FilterChain filterChain) throws ServletException, IOException {

    try {
      Object jwtObject = request.getAttribute("jwt");
      if(jwtObject != null) {
        Jwt jwt = (Jwt) jwtObject;
        ProviderIdentity providerIdentity = ProviderIdentity.fromSub(jwt.getSubject());
        Optional<UserIdentity> userIdentityOptional =
            userIdentityRepository.findByProviderAndProviderUserId(
                providerIdentity.provider(),
                providerIdentity.providerUserId()
            );

        if(userIdentityOptional.isEmpty()) {
          SaveUserCommand command = new SaveUserCommand(
              jwt.getClaimAsString("email"),
              jwt.getClaimAsString("name"),
              jwt.getClaimAsString("given_name"),
              jwt.getClaimAsString("family_name"),
              jwt.getClaimAsString("picture"),
              providerIdentity.provider(),
              providerIdentity.providerUserId()
          );
          saveUserService.execute(command);
        }
      }
    } catch (Exception e) {
      throw new ServletException("User verification failed", e);
    }

    filterChain.doFilter(request, response);
  }
}

