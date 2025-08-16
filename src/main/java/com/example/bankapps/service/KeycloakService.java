package com.example.bankapps.service;

import com.example.bankapps.model.dto.UserDto;

public interface KeycloakService {
    String getTokenAdmin();
    void createUser(String token , UserDto userDto);
    String getUserId(String token , String username);
    void deleteUser(String token, String userId);
}
