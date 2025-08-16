package com.example.bankapps.controller;


import com.example.bankapps.model.dto.AccountRequest;
import com.example.bankapps.service.RegisterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/account")
public class AccountController {

    private final RegisterService registerService;

    @PostMapping("/register")
    public ResponseEntity<Map<String,String>> register(@RequestBody @Valid AccountRequest accountRequest) {
        registerService.registerUser(accountRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "message", "User registered successfully"
        ));
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
