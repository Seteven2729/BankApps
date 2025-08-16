package com.example.bankapps.security;

import com.example.bankapps.commons.OAuthProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final OAuthProperties properties;

    @Bean
    public SecurityFilterChain securityFilterChain (HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(matcherRegistry ->
                        matcherRegistry
                                .requestMatchers(
                                        "/account/register",
                                        "/account/login",
                                        "/error").permitAll()
                                .anyRequest().authenticated())
                .oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(Customizer.withDefaults()));
        return http.build();
    }
    @Profile("!test") // only active when NOT test
    @Bean
    public JwtDecoder jwtDecoder (){
        NimbusJwtDecoder jwtDecoder = JwtDecoders.fromIssuerLocation(properties.getIssuer());
        OAuth2TokenValidator<Jwt> issuerValidator = JwtValidators.createDefaultWithIssuer(properties.getIssuer());
        OAuth2TokenValidator<Jwt> errorValidator = token -> {
            OAuth2TokenValidatorResult resultIssuer = issuerValidator.validate(token);
            if (resultIssuer.hasErrors())
                resultIssuer.getErrors().forEach(error ->
                        log.warn("Token Validation Failed : {}", error.getDescription()));
            return resultIssuer;
        };
         jwtDecoder.setJwtValidator(errorValidator);
         return jwtDecoder;
    }
}
