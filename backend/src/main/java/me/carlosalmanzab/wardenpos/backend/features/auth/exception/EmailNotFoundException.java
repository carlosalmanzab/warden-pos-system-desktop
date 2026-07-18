package me.carlosalmanzab.wardenpos.backend.features.auth.exception;

public class EmailNotFoundException extends RuntimeException {
  public EmailNotFoundException(String email) {
    super(String.format("El email %s no fue encontrado", email));
  }
}
