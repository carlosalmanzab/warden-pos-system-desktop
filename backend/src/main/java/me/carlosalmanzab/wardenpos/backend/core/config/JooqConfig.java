package me.carlosalmanzab.wardenpos.backend.core.config;

import static org.jooq.conf.ExecuteWithoutWhere.THROW;
import static org.jooq.conf.RenderNameCase.LOWER;

import org.jooq.RecordListenerProvider;
import org.jooq.conf.Settings;
import org.jooq.impl.DefaultRecordListenerProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JooqConfig {

  @Bean
  public Settings jooqAppSettings() {
    return new Settings().withExecuteDeleteWithoutWhere(THROW).withRenderNameCase(LOWER);
  }

  @Bean
  public RecordListenerProvider uuidv7RecordListenerProvider() {
    return new DefaultRecordListenerProvider(new UUIDv7RecordListener());
  }
}
