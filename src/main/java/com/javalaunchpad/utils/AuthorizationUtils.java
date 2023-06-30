package com.javalaunchpad.utils;

import org.h2.util.json.JSONArray;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Principal;
import java.util.Collection;

public class AuthorizationUtils {

    public static boolean isSuperAdmin() {
        var authorities = getUser().getAuthorities();
        return authorities.contains(new SimpleGrantedAuthority("SUPER_ADMIN"));
    }
    public static UserDetails getUser() {
        return (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}


