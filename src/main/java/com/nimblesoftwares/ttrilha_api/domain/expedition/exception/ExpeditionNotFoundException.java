package com.nimblesoftwares.ttrilha_api.domain.expedition.exception;

import com.nimblesoftwares.ttrilha_api.domain.DomainException;

public class ExpeditionNotFoundException extends DomainException {

  public ExpeditionNotFoundException(String message) {
    super(message);
  }
}

