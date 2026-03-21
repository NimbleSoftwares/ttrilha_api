package com.nimblesoftwares.ttrilha_api.adapter.in.web.exception;

import com.nimblesoftwares.ttrilha_api.application.user.exception.FriendshipPersistenceException;
import com.nimblesoftwares.ttrilha_api.domain.user.exception.CannotBlockSelfException;
import com.nimblesoftwares.ttrilha_api.domain.user.exception.CannotSendInviteToSelfException;
import com.nimblesoftwares.ttrilha_api.domain.user.exception.FriendshipAlreadyExistsException;
import com.nimblesoftwares.ttrilha_api.domain.user.exception.FriendshipSolicitationNotFoundException;
import com.nimblesoftwares.ttrilha_api.domain.user.exception.UserAlreadyBlockedException;
import com.nimblesoftwares.ttrilha_api.domain.user.exception.UserBlockedException;
import com.nimblesoftwares.ttrilha_api.domain.user.exception.UserBlockNotFoundException;
import com.nimblesoftwares.ttrilha_api.domain.user.exception.UserNotFoundException;
import io.micrometer.tracing.Tracer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class FriendshipExceptionHandler {

  private final Tracer tracer;

  public FriendshipExceptionHandler(Tracer tracer) {
    this.tracer = tracer;
  }

  @ExceptionHandler(CannotSendInviteToSelfException.class)
  public ProblemDetail handleCannotSendInviteToSelfException(CannotSendInviteToSelfException e) {
    String traceId = ExceptionHandlerHelper.getTraceId(tracer);
    log.warn(e.getMessage(), e);
    return ExceptionHandlerHelper.createProblemDetail(HttpStatus.UNPROCESSABLE_ENTITY, "Cannot send invite to yourself", e.getMessage(), traceId);
  }

  @ExceptionHandler(FriendshipSolicitationNotFoundException.class)
  public ProblemDetail handleFriendshipSolicitationNotFoundException(FriendshipSolicitationNotFoundException e) {
    String traceId = ExceptionHandlerHelper.getTraceId(tracer);
    log.warn(e.getMessage(), e);
    return ExceptionHandlerHelper.createProblemDetail(HttpStatus.NOT_FOUND, "Solicitation not found", e.getMessage(), traceId);
  }

  @ExceptionHandler(FriendshipAlreadyExistsException.class)
  public ProblemDetail handleFriendshipAlreadyExistsException(FriendshipAlreadyExistsException e) {
    String traceId = ExceptionHandlerHelper.getTraceId(tracer);
    log.warn(e.getMessage(), e);
    return ExceptionHandlerHelper.createProblemDetail(HttpStatus.CONFLICT, "Friendship already exists", e.getMessage(), traceId);
  }

  @ExceptionHandler(FriendshipPersistenceException.class)
  public ProblemDetail handleFriendshipPersistenceException(FriendshipPersistenceException e) {
    String traceId = ExceptionHandlerHelper.getTraceId(tracer);
    log.error(e.getMessage(), e);
    return ExceptionHandlerHelper.createProblemDetail(HttpStatus.INTERNAL_SERVER_ERROR, "An error has occurred", e.getMessage(), traceId);
  }

  @ExceptionHandler(UserNotFoundException.class)
  public ProblemDetail handleUserNotFoundException(UserNotFoundException e) {
    String traceId = ExceptionHandlerHelper.getTraceId(tracer);
    log.warn(e.getMessage(), e);
    return ExceptionHandlerHelper.createProblemDetail(HttpStatus.NOT_FOUND, "User not found", e.getMessage(), traceId);
  }

  @ExceptionHandler(CannotBlockSelfException.class)
  public ProblemDetail handleCannotBlockSelfException(CannotBlockSelfException e) {
    String traceId = ExceptionHandlerHelper.getTraceId(tracer);
    log.warn(e.getMessage(), e);
    return ExceptionHandlerHelper.createProblemDetail(HttpStatus.UNPROCESSABLE_ENTITY, "Cannot block yourself", e.getMessage(), traceId);
  }

  @ExceptionHandler(UserAlreadyBlockedException.class)
  public ProblemDetail handleUserAlreadyBlockedException(UserAlreadyBlockedException e) {
    String traceId = ExceptionHandlerHelper.getTraceId(tracer);
    log.warn(e.getMessage(), e);
    return ExceptionHandlerHelper.createProblemDetail(HttpStatus.CONFLICT, "User already blocked", e.getMessage(), traceId);
  }

  @ExceptionHandler(UserBlockedException.class)
  public ProblemDetail handleUserBlockedException(UserBlockedException e) {
    String traceId = ExceptionHandlerHelper.getTraceId(tracer);
    log.warn(e.getMessage(), e);
    return ExceptionHandlerHelper.createProblemDetail(HttpStatus.FORBIDDEN, "Action blocked", e.getMessage(), traceId);
  }

  @ExceptionHandler(UserBlockNotFoundException.class)
  public ProblemDetail handleUserBlockNotFoundException(UserBlockNotFoundException e) {
    String traceId = ExceptionHandlerHelper.getTraceId(tracer);
    log.warn(e.getMessage(), e);
    return ExceptionHandlerHelper.createProblemDetail(HttpStatus.NOT_FOUND, "Block not found", e.getMessage(), traceId);
  }
}
