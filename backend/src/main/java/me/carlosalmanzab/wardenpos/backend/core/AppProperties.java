package me.carlosalmanzab.wardenpos.backend.core;

import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "wardenpos.app")
public record AppProperties(
    String jwtSecret,
    Long jwtExpiration,
    List<String> pathWhiteList,
    List<String> corsAllowedOrigins) {}
