package com.nimblesoftwares.ttrilha_api.domain.user.exception;

import com.nimblesoftwares.ttrilha_api.domain.DomainException;

public class FriendshipAlreadyExistsException extends DomainException {

  public FriendshipAlreadyExistsException(String message) {
    super(message);
  }

  public FriendshipAlreadyExistsException(String message, Throwable cause) {
    super(message, cause);
  }
}
