package com.example.bankapps.commons;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app.sso")
@Getter
@Setter
public class OAuthProperties {
    private String url;
    private String issuer;
}
