package com.nimblesoftwares.ttrilha_api.domain.expedition.exception;

import com.nimblesoftwares.ttrilha_api.domain.DomainException;

public class ExpeditionInviteConflictException extends DomainException {
  public ExpeditionInviteConflictException(String message) {
    super(message);
  }
}

