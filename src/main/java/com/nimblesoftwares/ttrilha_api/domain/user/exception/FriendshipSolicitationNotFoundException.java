package com.nimblesoftwares.ttrilha_api.domain.user.exception;

import com.nimblesoftwares.ttrilha_api.domain.DomainException;

public class FriendshipSolicitationNotFoundException extends DomainException {

  public FriendshipSolicitationNotFoundException(String message) {
    super(message);
  }
}
