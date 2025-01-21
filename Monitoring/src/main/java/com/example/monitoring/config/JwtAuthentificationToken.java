package com.example.monitoring.config;

import org.springframework.security.authentication.AbstractAuthenticationToken;

public class JwtAuthentificationToken extends AbstractAuthenticationToken {

    private final String jwt;

    public JwtAuthentificationToken(String jwt) {
        super(null);
        this.jwt=jwt;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return this.jwt;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }
}
