package com.ecommerce.EcomProj.utils;

import com.ecommerce.EcomProj.Model.Users;
import com.ecommerce.EcomProj.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class AuthUtils {

    @Autowired
    UserRepository userRepository;

    public String loggedInEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Users user = userRepository.findByUserName(auth.getName())
                .orElseThrow(()-> new UsernameNotFoundException("User not found"));

        return user.getEmail();
    }

    public Users loggedInUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Users user = userRepository.findByUserName(auth.getName())
                .orElseThrow(()-> new UsernameNotFoundException("User not found"));

        return user;
    }
}
