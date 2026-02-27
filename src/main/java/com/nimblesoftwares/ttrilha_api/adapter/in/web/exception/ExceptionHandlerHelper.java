package com.nimblesoftwares.ttrilha_api.adapter.in.web.exception;

import io.micrometer.tracing.Tracer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

import java.util.Objects;
import java.util.UUID;

public class ExceptionHandlerHelper {

  static String getTraceId(Tracer tracer) {
    try {
        return Objects.requireNonNull(tracer.currentSpan()).context().traceId();
    } catch (Exception e) {
      //TODO: log.debug("Could not extract trace ID", e);
    }
    return UUID.randomUUID().toString();
  }

  static ProblemDetail createProblemDetail(HttpStatus status, String title, String detail, String traceId) {
    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, detail);
    problemDetail.setTitle(title);
    problemDetail.setProperty("traceId", traceId);
    return problemDetail;
  }
}
