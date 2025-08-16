package com.example.bankapps.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ErrorResponseDto {
    private String error;
    private String message;
}
