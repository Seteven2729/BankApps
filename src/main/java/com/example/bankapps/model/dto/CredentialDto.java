package com.example.bankapps.model.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CredentialDto {
    @Builder.Default
    private String type = "password";        // "password"
    private String value;       // password value
    @Builder.Default
    private boolean temporary = false;  // false for permanent
}
