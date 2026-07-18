package me.carlosalmanzab.wardenpos.backend.features.auth.repository;

import java.util.Optional;
import java.util.UUID;
import me.carlosalmanzab.wardenpos.jooq.generated.tables.pojos.RefreshTokens;

public interface RefreshTokenRepository {
  void delete(RefreshTokens refreshTokens);

  void revokeAllByUserId(UUID employeeId);

  Optional<RefreshTokens> findByToken(String token);
}
