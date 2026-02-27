package com.nimblesoftwares.ttrilha_api.adapter.in.web.exception;

import com.nimblesoftwares.ttrilha_api.application.user.exception.InvalidJwtClaimsException;
import com.nimblesoftwares.ttrilha_api.application.user.exception.UserPersistenceException;
import com.nimblesoftwares.ttrilha_api.domain.user.exception.InvalidProviderException;
import com.nimblesoftwares.ttrilha_api.domain.user.exception.UserAlreadyExistsException;
import com.nimblesoftwares.ttrilha_api.domain.user.exception.UserIdentityAlreadyExistsException;
import com.nimblesoftwares.ttrilha_api.domain.user.exception.UserIdentityPersistenceException;
import io.micrometer.tracing.Tracer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UserExceptionHandler {

  private final Tracer tracer;

  public UserExceptionHandler(Tracer tracer) {
    this.tracer = tracer;
  }

  @ExceptionHandler(UserAlreadyExistsException.class)
  public ProblemDetail handleUserAlreadyExistsException(UserAlreadyExistsException e) {
    String traceId = ExceptionHandlerHelper.getTraceId(tracer);
    //TODO: log.error(e.getMessage(), e);
    return ExceptionHandlerHelper.createProblemDetail(HttpStatus.CONFLICT, "User already exists", e.getMessage(), traceId);
  }

  @ExceptionHandler(UserPersistenceException.class)
  public ProblemDetail handleUserPersistenceException(UserPersistenceException e) {
    String traceId = ExceptionHandlerHelper.getTraceId(tracer);
    //TODO: log.error(e.getMessage(), e);
    return ExceptionHandlerHelper.createProblemDetail(HttpStatus.INTERNAL_SERVER_ERROR, "An error has occurred", e.getMessage(), traceId);
  }

  @ExceptionHandler(InvalidProviderException.class)
  public ProblemDetail handleInvalidProviderException(InvalidProviderException e) {
    String traceId = ExceptionHandlerHelper.getTraceId(tracer);
    //TODO: log.error(e.getMessage(), e);
    return ExceptionHandlerHelper.createProblemDetail(HttpStatus.BAD_REQUEST, "Invalid provider", e.getMessage(), traceId);

  }

  @ExceptionHandler(UserIdentityAlreadyExistsException.class)
  public ProblemDetail handleUserIdentityAlreadyExistsException(UserIdentityAlreadyExistsException e) {
    String traceId = ExceptionHandlerHelper.getTraceId(tracer);
    //TODO: log.error(e.getMessage(), e);
    return ExceptionHandlerHelper.createProblemDetail(HttpStatus.CONFLICT, "User identity already exists", e.getMessage(), traceId);

  }

  @ExceptionHandler(UserIdentityPersistenceException.class)
  public ProblemDetail handleUserIdentityPersistenceException(UserIdentityPersistenceException e) {
    String traceId = ExceptionHandlerHelper.getTraceId(tracer);
    //TODO: log.error(e.getMessage(), e);
    return ExceptionHandlerHelper.createProblemDetail(HttpStatus.INTERNAL_SERVER_ERROR, "An error has occurred", e.getMessage(), traceId);
  }

  @ExceptionHandler(InvalidJwtClaimsException.class)
  public ProblemDetail handleInvalidJwtClaimsException(InvalidJwtClaimsException e) {
    String traceId = ExceptionHandlerHelper.getTraceId(tracer);
    //TODO: log.error(e.getMessage(), e);
    return ExceptionHandlerHelper.createProblemDetail(HttpStatus.BAD_REQUEST, "Invalid JWT", e.getMessage(), traceId);
  }
}
