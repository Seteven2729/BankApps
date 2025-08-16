package com.example.bankapps.service.impl;

import com.example.bankapps.model.dao.Account;
import com.example.bankapps.model.dto.AccountRequest;
import com.example.bankapps.model.dto.CredentialDto;
import com.example.bankapps.model.dto.UserDto;
import com.example.bankapps.repository.AccountRepository;
import com.example.bankapps.service.KeycloakService;
import com.example.bankapps.service.RegisterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegisterServiceImpl implements RegisterService {
    public static final String BEARER = "Bearer ";
    private final AccountRepository accountRepository;
    private final KeycloakService keycloakService;
    @Transactional
    @Override
    public void registerUser(AccountRequest accountRequest) {
        String tokenAdmin = BEARER + keycloakService.getTokenAdmin();

        //hit api create user
        UserDto userDto = UserDto.builder()
                .email(accountRequest.getEmail())
                .username(accountRequest.getEmail())
                .enabled(true)
                .credentials(List.of(
                        CredentialDto.builder()
                                .value(accountRequest.getPassword())
                                .build()
                ))
                .build();

        boolean createUserApiSuccess = false;
        try {
            keycloakService.createUser(tokenAdmin, userDto);
            createUserApiSuccess = true;
            Account account = Account.builder()
                    .email(accountRequest.getEmail())
                    .build();
            accountRepository.save(account);
        } catch (Exception e) {
            if (createUserApiSuccess) {
                try {
                    String userId = keycloakService.getUserId(tokenAdmin,accountRequest.getEmail());
                    keycloakService.deleteUser( tokenAdmin, userId);
                } catch (Exception ex) {
                    log.error(ex.getMessage(), ex);
                }
            }
            throw e;
        }

    }
}
