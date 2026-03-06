package com.nimblesoftwares.ttrilha_api.domain.trail.exception;

import com.nimblesoftwares.ttrilha_api.domain.DomainException;

public class TrailAlreadyExistsException extends DomainException {
  public TrailAlreadyExistsException(String message) {
    super(message);
  }

  public TrailAlreadyExistsException(String message, Throwable cause) {
    super(message, cause);
  }
}
