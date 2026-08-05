package me.carlosalmanzab.wardenpos.backend.features.auth.service;

import java.time.LocalDateTime;
import java.util.Date;
import me.carlosalmanzab.wardenpos.backend.features.auth.dto.AuthResponse;
import me.carlosalmanzab.wardenpos.backend.features.auth.dto.LoginRequest;
import me.carlosalmanzab.wardenpos.backend.features.auth.dto.RegisterRequest;
import me.carlosalmanzab.wardenpos.backend.features.auth.exception.RefreshTokenExpiredException;
import me.carlosalmanzab.wardenpos.backend.features.auth.repository.EmployeeRepository;
import me.carlosalmanzab.wardenpos.backend.features.auth.repository.RefreshTokenRepository;
import me.carlosalmanzab.wardenpos.jooq.generated.tables.pojos.Employees;
import me.carlosalmanzab.wardenpos.jooq.generated.tables.pojos.RefreshTokens;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
  private final JwtService jwtService;
  private final EmployeeRepository employeeRepository;
  private final RefreshTokenRepository refreshTokenRepository;
  private final AuthenticationManager authenticationManager;

  public AuthService(
      JwtService jwtService,
      EmployeeRepository employeeRepository,
      RefreshTokenRepository refreshTokenRepository,
      AuthenticationManager authenticationManager) {
    this.jwtService = jwtService;
    this.employeeRepository = employeeRepository;
    this.refreshTokenRepository = refreshTokenRepository;
    this.authenticationManager = authenticationManager;
  }

  public AuthResponse login(LoginRequest request) {
    return null;
  }

  public void register(RegisterRequest request) {}

  public AuthResponse refresh(String refreshToken) {
    return refreshTokenRepository
        .findByToken(refreshToken)
        .map(this::assertValid)
        .map(this::revokeAllToken)
        .map(this::generateAuthResponse)
        .orElseThrow(RefreshTokenExpiredException::new);
  }

  private RefreshTokens assertValid(RefreshTokens refreshToken) {
    if (refreshToken.isRevoked() || refreshToken.expiresAt().isBefore(LocalDateTime.now())) {
      refreshTokenRepository.delete(refreshToken);
      throw new RefreshTokenExpiredException();
    }
    return refreshToken;
  }

  private Employees revokeAllToken(RefreshTokens refreshToken) {
    var employeeId = refreshToken.employeeId();
    refreshTokenRepository.revokeAllByUserId(employeeId);
    return employeeRepository.findById(employeeId);
  }

  private RefreshTokens createRefreshToken(Employees employee) {
    String token = jwtService.generateRefreshToken(employee.email());
    Date expiresAt = jwtService.validateAndParse(token).getExpiration();
    // RefreshTokens refreshToken = refreshTokenMapper.toNewRefreshToken(token, expiresAt, user);
    RefreshTokens refreshToken = new RefreshTokens();
    return refreshToken;
  }

  private AuthResponse generateAuthResponse(Employees employee) {
    String accessToken = jwtService.generateAccessToken(employee.email());
    RefreshTokens refreshToken = createRefreshToken(employee);
    return null;
  }
}
