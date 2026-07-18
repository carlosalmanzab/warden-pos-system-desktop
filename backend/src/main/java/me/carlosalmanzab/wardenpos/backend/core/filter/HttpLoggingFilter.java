package me.carlosalmanzab.wardenpos.backend.core.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
@RequiredArgsConstructor
public class HttpLoggingFilter extends OncePerRequestFilter {
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
