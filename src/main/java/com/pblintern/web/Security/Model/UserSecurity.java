package com.pblintern.web.Security.Model;

import com.pblintern.web.Entities.User;
import lombok.Data;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

@Data
public class UserSecurity implements Authentication {

    private User user;

    private List<GrantedAuthority> authorities;

    private boolean isAuthenticated;

    public UserSecurity(User user, List<GrantedAuthority> authorities) {
        this.user = user;
        this.authorities = authorities;
        this.isAuthenticated =true;
    }

    @Override
    public String getName() {
        return user.getEmail();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return user;
    }

    @Override
    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.isAuthenticated = isAuthenticated;
    }


}
