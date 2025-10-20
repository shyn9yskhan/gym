package com.shyn9yskhan.authentication_service.service;

import com.shyn9yskhan.authentication_service.client.UserServiceClient;
import com.shyn9yskhan.authentication_service.dto.UserDto;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class RemoteUserDetailsService implements UserDetailsService {
    private final UserServiceClient userServiceClient;

    public RemoteUserDetailsService(UserServiceClient userServiceClient) {
        this.userServiceClient = userServiceClient;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDto userDto = userServiceClient.getUserByUsername(username).getBody();
        if (userDto == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }

        String hashedPassword = userDto.getPassword();

        return User.builder()
                .username(userDto.getUsername())
                .password(hashedPassword)
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .build();
    }
}
