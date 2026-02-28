package com.nimblesoftwares.ttrilha_api.domain.user.exception;

import com.nimblesoftwares.ttrilha_api.domain.DomainException;

public class InvalidProviderException extends DomainException {

  public InvalidProviderException(String message) {
    super(message);
  }

  public InvalidProviderException(String message, Throwable cause) {
    super(message, cause);
  }
}
