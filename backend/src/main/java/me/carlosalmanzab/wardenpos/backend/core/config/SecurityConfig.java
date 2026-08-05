package me.carlosalmanzab.wardenpos.backend.core.config;

import java.util.List;
import me.carlosalmanzab.wardenpos.backend.core.AppProperties;
import me.carlosalmanzab.wardenpos.backend.core.filter.HttpLoggingFilter;
import me.carlosalmanzab.wardenpos.backend.core.filter.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
  private final AppProperties properties;

  public SecurityConfig(AppProperties properties) {
    this.properties = properties;
  }

  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowedOrigins(properties.corsAllowedOrigins());
    config.setAllowedMethods(List.of("*"));
    config.setAllowedHeaders(List.of("*"));

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);
    return source;
  }

  @Bean
  SecurityFilterChain securityFilterChain(
      HttpSecurity http,
      AuthenticationEntryPoint authEntryPoint,
      HttpLoggingFilter httpLoggingFilter,
      JwtAuthFilter jwtAuthFilter,
      AuthenticationProvider provider) {

    System.out.println(
        "[Security Filter] Request matchers White List: " + properties.pathWhiteList());

    var requestMatchers = properties.pathWhiteList().toArray(new String[0]);

    return http.cors(cors -> cors.configurationSource(corsConfigurationSource()))
        .csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(
            m ->
                m.requestMatchers(requestMatchers)
                    .permitAll()
                    .requestMatchers(HttpMethod.OPTIONS, "/**")
                    .permitAll()
                    .anyRequest()
                    .authenticated())
        .sessionManagement(m -> m.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authenticationProvider(provider)
        .exceptionHandling(e -> e.authenticationEntryPoint(authEntryPoint))
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
        .addFilterBefore(httpLoggingFilter, UsernamePasswordAuthenticationFilter.class)
        .build();
  }
}
