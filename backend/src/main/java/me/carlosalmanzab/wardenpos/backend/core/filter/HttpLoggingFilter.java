package me.carlosalmanzab.wardenpos.backend.core.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class HttpLoggingFilter extends OncePerRequestFilter {
  private static final Logger log = LoggerFactory.getLogger(HttpLoggingFilter.class);

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      FilterChain filterChain)
      throws ServletException, IOException {

    long start = System.currentTimeMillis();

    filterChain.doFilter(request, response);

    long duration = System.currentTimeMillis() - start;

    log.info(
        "HTTP {} {} → {} ({} ms)",
        request.getMethod(),
        request.getRequestURI(),
        response.getStatus(),
        duration);
  }
}
