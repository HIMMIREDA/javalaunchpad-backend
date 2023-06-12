package com.javalaunchpad.security.config;

import com.javalaunchpad.security.filter.JwtTokenFilter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

  private final JwtTokenFilter jwtAuthenticationFilter;
  private final UserDetailsService userDetailsService;
  private final DaoAuthenticationProvider daoAuthenticationProvider;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

    httpSecurity.headers().frameOptions().disable();

    httpSecurity.csrf().disable();
    //@formatter:off
    httpSecurity
          .authorizeHttpRequests()
          .requestMatchers("/api/auth/**").permitAll()
          .and()
          .authorizeHttpRequests()
          .requestMatchers("/api/tags/**").permitAll()
          .anyRequest().authenticated()
        .and()
          .sessionManagement()
          .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
          .exceptionHandling()
          .authenticationEntryPoint(
              (request, response, authException)
                -> response.sendError(
                    HttpServletResponse.SC_UNAUTHORIZED,
                    authException.getLocalizedMessage()
                  )
          )
        .and()
          .authenticationProvider(daoAuthenticationProvider)
          .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    //@formatter:on
    return httpSecurity.build();
  }
}
