package me.carlosalmanzab.wardenpos.backend.features.auth.exception;

public class RefreshTokenExpiredException extends RuntimeException {
  public RefreshTokenExpiredException(String message) {
    super(message);
  }

  public RefreshTokenExpiredException() {
    super("Sesión expirada. Inicie sesión nuevamente.");
  }
}
