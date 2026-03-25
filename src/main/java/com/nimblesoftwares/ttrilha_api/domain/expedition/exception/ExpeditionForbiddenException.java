package com.nimblesoftwares.ttrilha_api.domain.expedition.exception;

import com.nimblesoftwares.ttrilha_api.domain.DomainException;

public class ExpeditionForbiddenException extends DomainException {
  public ExpeditionForbiddenException(String message) {
    super(message);
  }
}

