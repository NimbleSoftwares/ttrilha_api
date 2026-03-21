package com.nimblesoftwares.ttrilha_api.domain.user.exception;

import com.nimblesoftwares.ttrilha_api.domain.DomainException;

public class CannotBlockSelfException extends DomainException {

  public CannotBlockSelfException(String message) {
    super(message);
  }
}

