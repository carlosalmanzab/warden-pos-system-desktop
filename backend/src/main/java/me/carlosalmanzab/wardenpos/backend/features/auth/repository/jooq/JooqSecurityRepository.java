package me.carlosalmanzab.wardenpos.backend.features.auth.repository.jooq;

import static me.carlosalmanzab.wardenpos.jooq.generated.Tables.EMPLOYEES;
import static me.carlosalmanzab.wardenpos.jooq.generated.Tables.ROLES;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import me.carlosalmanzab.wardenpos.backend.features.auth.model.SecurityUser;
import me.carlosalmanzab.wardenpos.backend.features.auth.repository.SecurityRepository;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Result;
import org.jooq.impl.DSL;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JooqSecurityRepository implements SecurityRepository {
  private final DSLContext dsl;

  @Override
  public Optional<SecurityUser> findByUsername(String username) {
    Field<Result<Record1<String>>> rolesField =
        DSL.multiset(dsl.select(ROLES.NAME).from(ROLES).where(ROLES.ID.eq(EMPLOYEES.ROLE_ID)))
            .as("roles");

    return dsl.select(
            EMPLOYEES.ID, EMPLOYEES.USERNAME, EMPLOYEES.PASSWORD, EMPLOYEES.IS_ACTIVE, rolesField)
        .from(EMPLOYEES)
        .where(EMPLOYEES.USERNAME.eq(username))
        .fetchOptional()
        .map(
            record -> {
              List<SimpleGrantedAuthority> authorities =
                  record.get(rolesField).stream()
                      .map(Record1::value1)
                      .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                      .toList();
              return new SecurityUser(
                  String.valueOf(record.get(EMPLOYEES.ID)),
                  record.get(EMPLOYEES.USERNAME),
                  record.get(EMPLOYEES.PASSWORD),
                  record.get(EMPLOYEES.IS_ACTIVE),
                  authorities);
            });
  }
}
