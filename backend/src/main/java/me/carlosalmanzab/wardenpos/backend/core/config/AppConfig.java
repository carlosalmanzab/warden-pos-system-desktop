package me.carlosalmanzab.wardenpos.backend.core.config;

import me.carlosalmanzab.wardenpos.backend.core.AppProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(AppProperties.class)
public class AppConfig {}
