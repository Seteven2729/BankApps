package com.example.bankapps.unitTest.service;

import com.example.bankapps.model.dao.Account;
import com.example.bankapps.model.dto.AccountRequest;
import com.example.bankapps.model.dto.UserDto;
import com.example.bankapps.repository.AccountRepository;
import com.example.bankapps.service.impl.KeycloakServiceImpl;
import com.example.bankapps.service.impl.RegisterServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisterServiceTest {

    @Mock
    private AccountRepository accountRepository;
    @Mock
    private KeycloakServiceImpl keycloakService;
    @InjectMocks
    private RegisterServiceImpl registerService;

    @Test
    void registerSuccess() {
        when(keycloakService.getTokenAdmin()).thenReturn("token-admin");
        doNothing().when(keycloakService).createUser(anyString(),any(UserDto.class));
        when(accountRepository.save(any(Account.class))).thenReturn(new Account());

        registerService.registerUser(AccountRequest.builder()
                .email("email")
                .password("password")
                .build());

        verify(keycloakService).getTokenAdmin();
        verify(keycloakService).createUser(anyString(),any(UserDto.class));
        verify(accountRepository).save(any(Account.class));
    }

    @Test
    void registerFailed() {
        when(keycloakService.getTokenAdmin()).thenReturn("token-admin");
        doNothing().when(keycloakService).createUser(anyString(),any(UserDto.class));
        when(accountRepository.save(any(Account.class))).thenThrow(new RuntimeException("error test"));
        when(keycloakService.getUserId(anyString(),anyString())).thenReturn("user id");
        doNothing().when(keycloakService).deleteUser(anyString(),anyString());

        Exception ex = assertThrows(Exception.class, () -> {
            registerService.registerUser(AccountRequest.builder()
                .email("email")
                .password("password")
                .build());
        });

        assertEquals("error test",ex.getMessage());

        verify(keycloakService).getTokenAdmin();
        verify(keycloakService).createUser(anyString(),any(UserDto.class));
        verify(accountRepository).save(any(Account.class));
        verify(keycloakService).getUserId(anyString(),anyString());
        verify(keycloakService).deleteUser(anyString(),anyString());
    }

    @Test
    void registerFailedApiFailedToo() {
        when(keycloakService.getTokenAdmin()).thenReturn("token-admin");
        doNothing().when(keycloakService).createUser(anyString(),any(UserDto.class));
        when(accountRepository.save(any(Account.class))).thenThrow(new RuntimeException("error test"));
        when(keycloakService.getUserId(anyString(),anyString())).thenThrow(new RuntimeException("new error test"));

        Exception ex = assertThrows(Exception.class, () -> {
            registerService.registerUser(AccountRequest.builder()
                    .email("email")
                    .password("password")
                    .build());
        });

        assertEquals("error test",ex.getMessage());

        verify(keycloakService).getTokenAdmin();
        verify(keycloakService).createUser(anyString(),any(UserDto.class));
        verify(accountRepository).save(any(Account.class));
        verify(keycloakService).getUserId(anyString(),anyString());
    }

    @Test
    void registerFailedApi() {
        when(keycloakService.getTokenAdmin()).thenReturn("token-admin");
        doThrow(new RuntimeException("api failed")).when(keycloakService).createUser(anyString(),any(UserDto.class));


        Exception ex = assertThrows(Exception.class, () -> {
            registerService.registerUser(AccountRequest.builder()
                    .email("email")
                    .password("password")
                    .build());
        });

        assertEquals("api failed",ex.getMessage());

        verify(keycloakService).getTokenAdmin();
        verify(keycloakService).createUser(anyString(),any(UserDto.class));

    }
}
