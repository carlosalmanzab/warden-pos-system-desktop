package me.carlosalmanzab.wardenpos.backend.config;

import org.jooq.conf.Settings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.jooq.conf.ExecuteWithoutWhere.THROW;
import static org.jooq.conf.RenderNameCase.LOWER;


@Configuration
public class JooqSettings {

    @Bean
    public Settings jooqSettings() {
        return new Settings()
                .withExecuteDeleteWithoutWhere(THROW)
                .withRenderNameCase(LOWER);
    }
}
