package me.carlosalmanzab.wardenpos.backend.features.auth.mapper;

import java.util.Date;
import me.carlosalmanzab.wardenpos.jooq.generated.tables.pojos.Employees;
import me.carlosalmanzab.wardenpos.jooq.generated.tables.pojos.RefreshTokens;

public interface RefreshTokensMapper {
    RefreshTokens toNewRefreshToken(String token, Date expiresAt, Employees employees);
}
