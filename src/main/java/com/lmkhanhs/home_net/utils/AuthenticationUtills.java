package com.lmkhanhs.home_net.utils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationUtills {
    public String getUserName(){
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}