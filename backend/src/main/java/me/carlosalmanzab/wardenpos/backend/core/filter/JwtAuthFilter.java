package me.carlosalmanzab.wardenpos.backend.core.filter;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.carlosalmanzab.wardenpos.backend.features.auth.service.JwtService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
  private final JwtService jwtService;
  private final UserDetailsService detailsService;

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest req, @NonNull HttpServletResponse res, @NonNull FilterChain chain)
      throws ServletException, IOException {

    if (HttpMethod.OPTIONS.toString().equalsIgnoreCase(req.getMethod())) {
      chain.doFilter(req, res);
      return;
    }

    if (SecurityContextHolder.getContext().getAuthentication() != null) {
      chain.doFilter(req, res);
      return;
    }

    String jwt = extractToken(req);

    if (!StringUtils.hasText(jwt)) {
      chain.doFilter(req, res);
      return;
    }

    if (SecurityContextHolder.getContext().getAuthentication() != null) {
      chain.doFilter(req, res);
      return;
    }
    try {
      var claims = jwtService.validateAndParse(jwt);
      var user = detailsService.loadUserByUsername(claims.getSubject());
      var authToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
      authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));

      SecurityContextHolder.getContext().setAuthentication(authToken);
    } catch (JwtException e) {
      log.warn("JWT authentication failed in JwtAuthFilter: {}", e.getMessage());
      SecurityContextHolder.clearContext();
    }

    chain.doFilter(req, res);
  }

  private String extractToken(HttpServletRequest req) {
    String header = req.getHeader(HttpHeaders.AUTHORIZATION);
    if (StringUtils.hasText(header) && header.startsWith(JwtService.TOKEN_TYPE + " ")) {
      return header.substring(7);
    }
    return null;
  }
}
