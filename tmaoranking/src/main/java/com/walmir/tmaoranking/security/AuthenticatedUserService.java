package com.walmir.tmaoranking.security;

import com.walmir.tmaoranking.domain.User;
import com.walmir.tmaoranking.exception.AccessDeniedException;
import com.walmir.tmaoranking.exception.BusinessException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticatedUserService {

    public User getLoggedUser() {
        return (User) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
    }

    public void checkOwnership(Long resourceOwnerId) {
        User loggedUser = getLoggedUser();
        if (!loggedUser.getId().equals(resourceOwnerId)) {
            throw new AccessDeniedException("Access denied: you don't own this resource");
        }
    }
}