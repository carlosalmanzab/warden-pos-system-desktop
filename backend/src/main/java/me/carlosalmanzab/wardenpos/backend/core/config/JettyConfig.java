package me.carlosalmanzab.wardenpos.backend.core.config;

import org.springframework.boot.jetty.servlet.JettyServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JettyConfig {

  @Bean
  public WebServerFactoryCustomizer<JettyServletWebServerFactory> jettyWebServer() {
    return factory ->
        factory.addServerCustomizers(
            server -> {
              server.setDumpAfterStart(false);
              server.setDumpBeforeStop(false);
              server.setStopAtShutdown(false);
              server.setStopTimeout(5000);
            });
  }
}
