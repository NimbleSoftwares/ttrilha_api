package com.nimblesoftwares.ttrilha_api.domain.user.exception;

import com.nimblesoftwares.ttrilha_api.domain.DomainException;

public class UserBlockNotFoundException extends DomainException {
  public UserBlockNotFoundException(String message) {
    super(message);
  }
}

