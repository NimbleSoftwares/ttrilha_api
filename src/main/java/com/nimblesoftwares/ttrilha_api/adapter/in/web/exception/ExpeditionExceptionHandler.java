package com.nimblesoftwares.ttrilha_api.adapter.in.web.exception;

import com.nimblesoftwares.ttrilha_api.domain.expedition.exception.ExpeditionForbiddenException;
import com.nimblesoftwares.ttrilha_api.domain.expedition.exception.ExpeditionInviteConflictException;
import com.nimblesoftwares.ttrilha_api.domain.expedition.exception.ExpeditionInviteNotFoundException;
import com.nimblesoftwares.ttrilha_api.domain.expedition.exception.ExpeditionNotFoundException;
import io.micrometer.tracing.Tracer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExpeditionExceptionHandler {

  private final Tracer tracer;

  public ExpeditionExceptionHandler(Tracer tracer) {
    this.tracer = tracer;
  }

  @ExceptionHandler(ExpeditionNotFoundException.class)
  public ProblemDetail handleNotFound(ExpeditionNotFoundException e) {
    String traceId = ExceptionHandlerHelper.getTraceId(tracer);
    log.warn(e.getMessage(), e);
    return ExceptionHandlerHelper.createProblemDetail(HttpStatus.NOT_FOUND, "Expedition not found", e.getMessage(), traceId);
  }

  @ExceptionHandler(ExpeditionForbiddenException.class)
  public ProblemDetail handleForbidden(ExpeditionForbiddenException e) {
    String traceId = ExceptionHandlerHelper.getTraceId(tracer);
    log.warn(e.getMessage(), e);
    return ExceptionHandlerHelper.createProblemDetail(HttpStatus.FORBIDDEN, "Forbidden", e.getMessage(), traceId);
  }

  @ExceptionHandler(ExpeditionInviteNotFoundException.class)
  public ProblemDetail handleInviteNotFound(ExpeditionInviteNotFoundException e) {
    String traceId = ExceptionHandlerHelper.getTraceId(tracer);
    log.warn(e.getMessage(), e);
    return ExceptionHandlerHelper.createProblemDetail(HttpStatus.NOT_FOUND, "Invite not found", e.getMessage(), traceId);
  }

  @ExceptionHandler(ExpeditionInviteConflictException.class)
  public ProblemDetail handleInviteConflict(ExpeditionInviteConflictException e) {
    String traceId = ExceptionHandlerHelper.getTraceId(tracer);
    log.warn(e.getMessage(), e);
    return ExceptionHandlerHelper.createProblemDetail(HttpStatus.CONFLICT, "Invite conflict", e.getMessage(), traceId);
  }
}

