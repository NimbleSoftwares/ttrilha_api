package com.nimblesoftwares.ttrilha_api.domain.user.exception;

import com.nimblesoftwares.ttrilha_api.domain.DomainException;

public class UserIdentityAlreadyExistsException extends DomainException {
  public UserIdentityAlreadyExistsException(String message) {
    super(message);
  }

  public UserIdentityAlreadyExistsException(String message, Throwable cause) {
    super(message, cause);
  }
}
