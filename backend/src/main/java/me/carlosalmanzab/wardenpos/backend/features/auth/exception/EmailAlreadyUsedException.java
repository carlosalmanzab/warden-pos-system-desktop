package me.carlosalmanzab.wardenpos.backend.features.auth.exception;

public class EmailAlreadyUsedException extends RuntimeException {
  public EmailAlreadyUsedException(String email) {
    super("El email '" + email + "' ya se encuentra registrado en el sistema.");
  }
}
