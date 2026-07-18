package me.carlosalmanzab.wardenpos.backend.common.exception;

import java.net.URI;
import java.time.Instant;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

public class BaseExceptionHandler {
  protected ResponseEntity<ProblemDetail> buildResponse(
      HttpStatus status, String title, String detail, WebRequest request) {

    ProblemDetail problem = ProblemDetail.forStatusAndDetail(status, detail);
    String path = request.getDescription(false).replace("uri=", "");

    problem.setTitle(title);
    problem.setInstance(URI.create(path));
    problem.setProperty("timestamp", Instant.now());
    problem.setProperty("trace", UUID.randomUUID().toString());

    return ResponseEntity.status(status).body(problem);
  }
}
