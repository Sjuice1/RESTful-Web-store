package com.example.RESTftulSN.enums;

import org.springframework.security.core.GrantedAuthority;

public enum USER_ROLE implements GrantedAuthority {
    ROLE_GUEST,ROLE_VERIFIED,ROLE_MODERATOR,ROLE_ADMIN;

    @Override
    public String getAuthority() {
        return this.toString();
    }
}
