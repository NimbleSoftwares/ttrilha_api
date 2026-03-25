package com.nimblesoftwares.ttrilha_api.domain.expedition.exception;

import com.nimblesoftwares.ttrilha_api.domain.DomainException;

public class ExpeditionInviteNotFoundException extends DomainException {
  public ExpeditionInviteNotFoundException(String message) {
    super(message);
  }
}

