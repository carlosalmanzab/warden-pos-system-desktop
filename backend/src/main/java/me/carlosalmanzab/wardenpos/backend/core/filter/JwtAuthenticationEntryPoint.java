package me.carlosalmanzab.wardenpos.backend.core.filter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
  private final HandlerExceptionResolver handlerExceptionResolver;

  @Override
  public void commence(
      @NonNull HttpServletRequest req,
      @NonNull HttpServletResponse res,
      @NonNull AuthenticationException ex) {
    handlerExceptionResolver.resolveException(req, res, null, ex);
  }
}
