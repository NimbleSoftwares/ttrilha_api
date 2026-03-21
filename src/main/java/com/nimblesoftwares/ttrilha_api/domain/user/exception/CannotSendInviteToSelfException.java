package com.nimblesoftwares.ttrilha_api.domain.user.exception;

import com.nimblesoftwares.ttrilha_api.domain.DomainException;

public class CannotSendInviteToSelfException extends DomainException {

  public CannotSendInviteToSelfException(String message) {
    super(message);
  }
}

