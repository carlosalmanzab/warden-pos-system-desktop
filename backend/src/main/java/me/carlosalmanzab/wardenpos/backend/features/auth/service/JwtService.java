package me.carlosalmanzab.wardenpos.backend.features.auth.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import me.carlosalmanzab.wardenpos.backend.core.AppProperties;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtService {
  private final AppProperties properties;
  public static final String TOKEN_TYPE = "Bearer";
  private SecretKey signingKey;
  private long expiration;
  private long refreshExpiration;

  @PostConstruct
  void init() {
    this.signingKey = Keys.hmacShaKeyFor(properties.jwtSecret().getBytes(StandardCharsets.UTF_8));
    this.expiration = properties.jwtExpiration();
    this.refreshExpiration = expiration * 14 * 24;
  }

  private String buildToken(String subject, long expiration) {
    Date now = new Date();
    return Jwts.builder()
        .subject(subject)
        .issuedAt(now)
        .expiration(new Date(now.getTime() + expiration))
        .signWith(signingKey)
        .compact();
  }

  public String generateAccessToken(String subject) {
    return buildToken(subject, expiration);
  }

  public String generateRefreshToken(String subject) {
    return buildToken(subject, refreshExpiration);
  }

  public Claims validateAndParse(String jwt) {
    return Jwts.parser().verifyWith(signingKey).build().parseSignedClaims(jwt).getPayload();
  }

  public boolean validate(String jwt, UserDetails userDetails) {
    Claims claims = validateAndParse(jwt);
    return claims.getSubject().equals(userDetails.getUsername())
        && claims.getExpiration().after(new Date());
  }
}
