package com.shyn9yskhan.authentication_service.config;

import com.shyn9yskhan.authentication_service.client.UserServiceClient;
import com.shyn9yskhan.authentication_service.dto.UserDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CustomUserDetailsService implements UserDetailsService {
    private final UserServiceClient userServiceClient;

    public CustomUserDetailsService(UserServiceClient userServiceClient) {
        this.userServiceClient = userServiceClient;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ResponseEntity<UserDto> userServiceClientResponse = userServiceClient.getUserByUsername(username);
        UserDto userDto = userServiceClientResponse.getBody();

        if (userDto == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }

        return new CustomUserDetails(userDto.getUsername(), userDto.getPassword());
    }
}
