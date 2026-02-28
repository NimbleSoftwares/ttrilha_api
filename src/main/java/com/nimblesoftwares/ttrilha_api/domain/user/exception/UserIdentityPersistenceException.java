package com.nimblesoftwares.ttrilha_api.domain.user.exception;

import com.nimblesoftwares.ttrilha_api.domain.DomainException;

public class UserIdentityPersistenceException extends DomainException {
  public UserIdentityPersistenceException(String message) {
    super(message);
  }

  public UserIdentityPersistenceException(String message, Throwable cause) {
    super(message, cause);
  }
}
