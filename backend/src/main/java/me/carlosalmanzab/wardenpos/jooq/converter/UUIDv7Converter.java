package me.carlosalmanzab.wardenpos.jooq.converter;

import java.util.UUID;
import org.jooq.impl.AbstractConverter;

public class UUIDv7Converter extends AbstractConverter<String, UUID> {

  public UUIDv7Converter() {
    super(String.class, UUID.class);
  }

  @Override
  public UUID from(String databaseObject) {
    if (databaseObject == null || databaseObject.isBlank()) {
      return null;
    }
    try {
      return UUID.fromString(databaseObject);
    } catch (IllegalArgumentException e) {
      return null;
    }
  }

  @Override
  public String to(UUID userObject) {
    if (userObject == null) {
      return null;
    }
    return userObject.toString();
  }
}
