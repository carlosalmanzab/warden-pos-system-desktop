package me.carlosalmanzab.wardenpos.backend.features.auth.model;

import java.util.Collection;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@NullMarked
public record SecurityUser(
    String id,
    String username,
    String password,
    boolean active,
    Collection<? extends GrantedAuthority> authorities)
    implements UserDetails {
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public boolean isEnabled() {
    return active;
  }
}
