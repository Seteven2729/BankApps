package com.example.bankapps.controller;


import com.example.bankapps.model.dto.AccountRequest;
import com.example.bankapps.model.dto.TokenDto;
import com.example.bankapps.service.LoginService;
import com.example.bankapps.service.RegisterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/account")
public class AccountController {

    private final RegisterService registerService;
    private final LoginService loginService;

    @PostMapping("/register")
    public ResponseEntity<Map<String,String>> register(@RequestBody @Valid AccountRequest accountRequest) {
        registerService.registerUser(accountRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "message", "User registered successfully"
        ));
    }

    @GetMapping("/login")
    public ResponseEntity<Map<String, Serializable>> login(@RequestBody @Valid AccountRequest accountRequest) {
        TokenDto dto = loginService.getToken(accountRequest);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of(
                "message","login success",
                "token",dto.getToken(),
                "expire_in",dto.getExpiresIn()
        ));
    }
}
