package com.javalaunchpad.utils;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class AuthorizationUtils {

    public static boolean isSuperAdmin() {
        UserDetails  user = getUser() ;
        if (user != null){
            var authorities = getUser().getAuthorities();
            return authorities.contains(new SimpleGrantedAuthority("SUPER_ADMIN"));
        }
        return false ;
    }
    public static UserDetails getUser() {
        Object principal =  SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal != "anonymousUser"){
            return (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }
        return null ;
    }
}


