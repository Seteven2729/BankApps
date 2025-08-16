package com.example.bankapps.integration;


import com.example.bankapps.BankAppsApplication;
import com.example.bankapps.TestcontainersConfiguration;
import com.example.bankapps.commons.Utility;
import com.example.bankapps.model.dao.Account;
import com.example.bankapps.model.dto.ErrorResponseDto;
import com.example.bankapps.model.dto.StatementDto;
import com.example.bankapps.model.dto.TransactionRequest;
import com.example.bankapps.service.TransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {BankAppsApplication.class, TestcontainersConfiguration.class})
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @MockitoBean
    private TransactionService transactionService;

    @BeforeEach
    void setup() {
        // Clean up before each test
        objectMapper = new ObjectMapper();
    }


    private JwtAuthenticationToken createAuthToken(String email) {
        Jwt jwt = Jwt.withTokenValue("fake-token")
                .header("alg", "none")
                .claim("preferred_username", email)
                .build();
        return new JwtAuthenticationToken(jwt);
    }

    @Test
    void testWithdrawMoney() throws Exception {
        String email = "user@example.com";
        TransactionRequest request = TransactionRequest.builder()
                .email(email)
                .amount(BigDecimal.valueOf(50000L))
                .reqId("123")
                .build();

        Account mockAccount = new Account();
        mockAccount.setEmail(email);
        mockAccount.setBalance(BigDecimal.valueOf(150000L));

        when(transactionService.withdrawMoney(any(TransactionRequest.class))).thenReturn(mockAccount);
        mockMvc.perform(post("/transaction/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .principal(createAuthToken(email)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.balance").value(Utility.toIDR(BigDecimal.valueOf(150000L))));
    }

    @Test
    void testDepositMoney() throws Exception {
        String email = "user@example.com";
        TransactionRequest request = TransactionRequest.builder()
                .email(email)
                .amount(BigDecimal.valueOf(70000L))
                .reqId("123")
                .build();

        Account mockAccount = new Account();
        mockAccount.setEmail(email);
        mockAccount.setBalance(BigDecimal.valueOf(200000L));

        when(transactionService.depositMoney(any(TransactionRequest.class))).thenReturn(mockAccount);

        mockMvc.perform(post("/transaction/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .principal(createAuthToken(email)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.balance").value(Utility.toIDR(BigDecimal.valueOf(200000L))));
    }

    @Test
    void testGetStatement() throws Exception {
        String email = "user@example.com";
        StatementDto mockStatement = new StatementDto();
        mockStatement.setTransactions(List.of());

        when(transactionService.getStatement(email)).thenReturn(mockStatement);
        mockMvc.perform(get("/transaction/statement")
                        .param("email", email)
                        .principal(createAuthToken(email)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transactions").isArray());
    }

    @Test
    void testGetBalance() throws Exception {
        String email = "user@example.com";
        when(transactionService.getBalance(email)).thenReturn("150000");
        mockMvc.perform(get("/transaction/balance")
                        .param("email", email)
                        .principal(createAuthToken(email)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.['current balance']").value("150000"));
    }


    @Test
    void testWithdrawMoneyError() throws Exception {
        String email = "user@example.com";
        TransactionRequest request = TransactionRequest.builder()
                .email(email)
                .amount(BigDecimal.valueOf(50000L))
                .reqId("123")
                .build();
        ErrorResponseDto dto = new ErrorResponseDto("UNAUTHORIZED", "asdasd");
        mockMvc.perform(post("/transaction/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .principal(createAuthToken("asd")))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value(dto.getError()));
    }

    @Test
    void testDepositMoneyError() throws Exception {
        String email = "user@example.com";
        TransactionRequest request = TransactionRequest.builder()
                .email(email)
                .amount(BigDecimal.valueOf(70000L))
                .reqId("123")
                .build();


        ErrorResponseDto dto = new ErrorResponseDto("UNAUTHORIZED", "asdasd");

        mockMvc.perform(post("/transaction/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .principal(createAuthToken("asdasd")))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value(dto.getError()));
    }

    @Test
    void testGetStatementError() throws Exception {
        String email = "user@example.com";

        ErrorResponseDto dto = new ErrorResponseDto("UNAUTHORIZED", "asdasd");
        mockMvc.perform(get("/transaction/statement")
                        .param("email", email)
                        .principal(createAuthToken("asdasdasd")))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value(dto.getError()));
    }

    @Test
    void testGetBalanceError() throws Exception {
        String email = "user@example.com";
        ErrorResponseDto dto = new ErrorResponseDto("UNAUTHORIZED", "asdasd");
        mockMvc.perform(get("/transaction/balance")
                        .param("email", email)
                        .principal(createAuthToken("asdasd")))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value(dto.getError()));


    }
}
