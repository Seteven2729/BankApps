package com.example.bankapps.service;

import com.example.bankapps.model.dto.AccountRequest;
import com.example.bankapps.model.dto.TokenDto;

public interface LoginService {
    TokenDto getToken (AccountRequest accountRequest);
}
