package me.carlosalmanzab.wardenpos.backend.features.auth.repository;

import java.util.Optional;
import me.carlosalmanzab.wardenpos.backend.features.auth.model.SecurityUser;

public interface SecurityRepository {
  Optional<SecurityUser> findByUsername(String username);
}
