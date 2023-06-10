package com.javalaunchpad.security;

import org.springframework.context.annotation.Configuration;

//@EnableWebSecurity
@Configuration
public class SecurityConfig {

/*

    @Bean
    public  SecurityFilterChain  securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.cors().configurationSource(corsConfigurationSource());
        httpSecurity.csrf().disable();
        httpSecurity.headers().frameOptions().disable();
        httpSecurity.authorizeHttpRequests().requestMatchers("/h2-console/**").permitAll();
        httpSecurity.authorizeHttpRequests().requestMatchers("/api/posts/**").permitAll();
        httpSecurity.authorizeHttpRequests().anyRequest().authenticated();
        httpSecurity.formLogin();
        return httpSecurity.build() ;
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.addAllowedOrigin("http://localhost:8081");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
*/


}
