package com.badminton.store.service.impl;

import com.badminton.store.service.AuthenticationFacadeService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFacadeServiceImpl implements AuthenticationFacadeService {
    @Override
    public Authentication getAuthentication() {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            throw new BadCredentialsException("Invalid token");
        }
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
