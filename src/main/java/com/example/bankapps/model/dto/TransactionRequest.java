package com.example.bankapps.model.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionRequest {
    @NotBlank(message = "Email must be not blank/empty")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Request ID must not be blank")
    @Size(max = 100, message = "Request ID must be at most 100 characters")
    private String reqId;

    @NotNull(message = "Amount must not be null")
    @DecimalMin(value = "0.01", inclusive = true, message = "Amount must be greater than zero")
    @Digits(integer = 15, fraction = 2, message = "Amount can have up to 2 decimal places")
    private BigDecimal amount;

}
