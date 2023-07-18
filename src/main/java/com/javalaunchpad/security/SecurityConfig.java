package com.javalaunchpad.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.HttpBasicDsl;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity(debug = true)
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    private static final String[] AUTH_WHITELIST = {
            "/h2-console/**",
            "/api/posts/**",
            "/login",
    };

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.cors(Customizer.withDefaults());
        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        httpSecurity.formLogin( httpSecurityFormLoginConfigurer -> httpSecurityFormLoginConfigurer
                .successHandler((request, response, authentication) -> response.setStatus(HttpStatus.OK.value()))
                .failureHandler((request, response, exception) -> response.setStatus(HttpStatus.UNAUTHORIZED.value()))
        );
        httpSecurity.httpBasic(Customizer.withDefaults());
        httpSecurity.logout().deleteCookies("JSESSIONID").clearAuthentication(true);
        httpSecurity.headers().frameOptions().disable();
        httpSecurity
                .authorizeHttpRequests(
                        authorizationManagerRequestMatcherRegistry -> authorizationManagerRequestMatcherRegistry.requestMatchers(AUTH_WHITELIST).permitAll()
                                .anyRequest().authenticated()
                )
                .exceptionHandling().authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));
        return httpSecurity.build();
    }


    @Bean
    CorsConfiguration corsConfiguration() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:8081/"));
        configuration.setAllowedMethods(Arrays.asList("DELETE", "GET", "POST", "PATCH", "PUT"));
        configuration.setAllowedHeaders(Arrays.asList("Content-Type", "X-XSRF-TOKEN", "Authorization"));
        configuration.setAllowCredentials(true);
        return configuration;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration());
        return source;
    }
}
