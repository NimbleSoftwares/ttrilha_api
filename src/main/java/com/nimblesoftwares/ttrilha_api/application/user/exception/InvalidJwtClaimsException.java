package com.nimblesoftwares.ttrilha_api.application.user.exception;

import com.nimblesoftwares.ttrilha_api.application.ApplicationException;

public class InvalidJwtClaimsException extends ApplicationException {

  public InvalidJwtClaimsException(String message) {
    super(message);
  }

  public InvalidJwtClaimsException(String message, Throwable cause) {
    super(message, cause);
  }
}
