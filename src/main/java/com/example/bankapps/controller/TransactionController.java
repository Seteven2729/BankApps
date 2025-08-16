package com.example.bankapps.controller;

import com.example.bankapps.commons.Utility;
import com.example.bankapps.exception.UnauthorizedException;
import com.example.bankapps.model.dao.Account;
import com.example.bankapps.model.dto.TransactionRequest;
import com.example.bankapps.service.TransactionService;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/transaction")
@Validated
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping("/withdraw")
    public ResponseEntity<Map<String, Serializable>> withdrawMoney(@RequestBody TransactionRequest request, Authentication authentication) {
        Jwt jwt = ((JwtAuthenticationToken) authentication).getToken();
        String tokenEmail = jwt.getClaimAsString("preferred_username");
        if (!request.getEmail().equalsIgnoreCase(tokenEmail)) {
            throw new UnauthorizedException("Email in request does not match token");
        }
        Account account = transactionService.withdrawMoney(request);
        return ResponseEntity.ok(Map.of(
                "email",account.getEmail(),
                "balance", Utility.toIDR(account.getBalance())));
    }

    @PostMapping("/deposit")
    public String depositMoney() {
        return "deposit";
    }

    @GetMapping("/statement")
    public String getStatement() {
        return "statement";
    }

    @GetMapping("/balance")
    public ResponseEntity<Map<String, String>> getBalance(
            @RequestParam("email")
            @NotBlank(message = "Email must not be blank")
            @Email(message = "Invalid email format") String email,
            Authentication authentication) {

        Jwt jwt = ((JwtAuthenticationToken) authentication).getToken();
        String tokenEmail = jwt.getClaimAsString("preferred_username");
        if (!email.equalsIgnoreCase(tokenEmail)) {
            throw new UnauthorizedException("Email in request does not match token");
        }
        return ResponseEntity.ok(Map.of(
                "email", email,
                "current balance", transactionService.getBalance(email)
        ));
    }
}
