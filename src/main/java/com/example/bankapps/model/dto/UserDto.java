package com.example.bankapps.model.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private String username;
    private String email;
    private boolean enabled;
    private List<CredentialDto> credentials;
}
