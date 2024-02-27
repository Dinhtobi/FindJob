package com.pblintern.web.Security.Model;

import lombok.Data;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Data
public class TokenSecurity extends AbstractAuthenticationToken {

    private String token;

    public TokenSecurity(String token) {
        super(null);
        this.token = token;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }
}
