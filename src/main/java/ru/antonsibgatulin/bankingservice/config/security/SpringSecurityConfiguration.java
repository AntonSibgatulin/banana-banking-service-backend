package ru.antonsibgatulin.bankingservice.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import ru.antonsibgatulin.bankingservice.except.AccessDeniedForUnAutorizationException;
import ru.antonsibgatulin.bankingservice.service.jwt.JwtAuthenticationFilter;
/*
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


 */

@Configuration
@EnableWebSecurity
@EnableWebMvc
@RequiredArgsConstructor
public class SpringSecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf().disable().exceptionHandling().disable()
                .exceptionHandling().accessDeniedHandler((request, response, ex) -> {

                    throw new AccessDeniedForUnAutorizationException("Please auth");

                }).and()
                .authorizeHttpRequests()
                //.requestMatchers("/api/v1/seller/**", "/api/v1/reg/**", "/api/v1/auth/**").permitAll()
                //.requestMatchers("/api/**").authenticated()
                .requestMatchers("/api/auth/signup", "api/auth/signin", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);


        return http.build();
    }


}