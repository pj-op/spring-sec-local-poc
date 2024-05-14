package com.poc.service.impl;

import com.poc.config.UserDetailsConfig;
import com.poc.repo.UserInfoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserInfoService implements UserDetailsService {

    @Autowired
    private UserInfoRepo userInfoRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userInfoRepo
                .findByUserName(username)
                .map(UserDetailsConfig::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found for " + username));
    }
}
