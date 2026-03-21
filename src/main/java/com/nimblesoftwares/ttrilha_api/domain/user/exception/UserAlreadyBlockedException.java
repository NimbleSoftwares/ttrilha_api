package com.nimblesoftwares.ttrilha_api.domain.user.exception;

import com.nimblesoftwares.ttrilha_api.domain.DomainException;

public class UserAlreadyBlockedException extends DomainException {

  public UserAlreadyBlockedException(String message) {
    super(message);
  }
}

