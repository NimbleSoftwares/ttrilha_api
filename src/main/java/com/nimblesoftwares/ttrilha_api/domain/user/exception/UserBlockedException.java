package com.nimblesoftwares.ttrilha_api.domain.user.exception;

import com.nimblesoftwares.ttrilha_api.domain.DomainException;

public class UserBlockedException extends DomainException {

  public UserBlockedException(String message) {
    super(message);
  }
}

