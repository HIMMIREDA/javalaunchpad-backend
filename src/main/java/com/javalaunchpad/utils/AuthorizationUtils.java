package com.javalaunchpad.utils;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class AuthorizationUtils {

    public static boolean isSuperAdmin() {
        var authorities = getUser().getAuthorities();
        return authorities.contains(new SimpleGrantedAuthority("SUPER_ADMIN"));
    }
    public static UserDetails getUser() {
        return (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}


