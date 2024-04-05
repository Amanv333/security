package com.security30.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

@Configuration
public class SecurityConfiguration {

    private JwtRequestFilter jwtFilter;

    public SecurityConfiguration(JwtRequestFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

                // shortCut(cd)^2
        http.csrf().disable().cors().disable();
        http.addFilterBefore(jwtFilter, AuthorizationFilter.class);
        //(in Development any request will authorize //haap)
        http.authorizeHttpRequests()
                .requestMatchers("/api/v1/users/login","/api/v1/users/addUser").permitAll()
                .requestMatchers("/api/v1/country").hasRole("ADMIN")
                .requestMatchers("/api/v1/country/visit").hasRole("USER")
                .anyRequest().authenticated()
                .and()
                .oauth2Login();
        return http.build();

    }

}
