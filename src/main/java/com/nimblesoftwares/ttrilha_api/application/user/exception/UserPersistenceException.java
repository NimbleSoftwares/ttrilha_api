package com.nimblesoftwares.ttrilha_api.application.user.exception;

import com.nimblesoftwares.ttrilha_api.application.ApplicationException;

public class UserPersistenceException extends ApplicationException {
  public UserPersistenceException(String message) {
    super(message);
  }

  public UserPersistenceException(String message, Throwable cause) {
    super(message, cause);
  }
}
