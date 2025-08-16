package com.example.bankapps.unitTest.service;

import com.example.bankapps.model.dto.AccountRequest;
import com.example.bankapps.model.dto.TokenDto;
import com.example.bankapps.service.impl.KeycloakServiceImpl;
import com.example.bankapps.service.impl.LoginServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {

    @Mock
    private KeycloakServiceImpl keycloakService;

    @InjectMocks
    private LoginServiceImpl loginService;

    @Test
    void getTokenSuccess(){
        Mockito.when(keycloakService.getToken(Mockito.any(AccountRequest.class))).thenReturn(new TokenDto());
        loginService.getToken(new AccountRequest());
        Mockito.verify(keycloakService).getToken(Mockito.any(AccountRequest.class));
    }
}
