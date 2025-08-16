package com.example.bankapps.service.impl;

import com.example.bankapps.model.dto.AccountRequest;
import com.example.bankapps.model.dto.TokenDto;
import com.example.bankapps.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {
    private final KeycloakServiceImpl keycloakService;
    @Override
    public TokenDto getToken(AccountRequest accountRequest) {
        return keycloakService.getToken(accountRequest);
    }
}
