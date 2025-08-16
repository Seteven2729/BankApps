package com.example.bankapps.integration;

import com.example.bankapps.BankAppsApplication;
import com.example.bankapps.TestSecurityConfig;
import com.example.bankapps.TestcontainersConfiguration;
import com.example.bankapps.gateway.KeycloakAdminGateway;
import com.example.bankapps.model.dto.AccountRequest;
import com.example.bankapps.model.dto.ClientRolesDto;
import com.example.bankapps.model.dto.UserDto;
import com.example.bankapps.repository.AccountRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(classes = {BankAppsApplication.class, TestcontainersConfiguration.class, TestSecurityConfig.class})
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class AccountControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private AccountRepository accountRepository;
    @MockitoBean
    private KeycloakAdminGateway keycloakAdminGateway;

    private ObjectMapper objectMapper;


    @BeforeEach
    void setup() {
        // Clean up before each test
        objectMapper = new ObjectMapper();
        accountRepository.deleteAll();
    }

    @Test
    void registerUser_success() throws Exception {

        ObjectNode adminToken = objectMapper.createObjectNode();
        adminToken.put("access_token", "asdasd");

        ObjectNode node = objectMapper.createObjectNode();
        node.put("id", "asdasd");
        ArrayNode arrayNode = objectMapper.createArrayNode();
        arrayNode.add(node);

        when(keycloakAdminGateway.getAdminToken(any())).thenReturn(adminToken);
        Mockito.doNothing().when(keycloakAdminGateway).createUser(Mockito.anyString(), Mockito.any(UserDto.class));
        Mockito.when(keycloakAdminGateway.getClientId(Mockito.anyString(), Mockito.anyString())).thenReturn(arrayNode);
        Mockito.when(keycloakAdminGateway.getClientRoles(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(new ClientRolesDto());
        Mockito.when(keycloakAdminGateway.getUserId(Mockito.anyString(), Mockito.anyString())).thenReturn(arrayNode);
        Mockito.doNothing().when(keycloakAdminGateway).assignRole(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyList());

        AccountRequest request = AccountRequest.builder()
                .email("test@example.com")
                .password("password")
                .build();

        mockMvc.perform(post("/account/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("User registered successfully"));

        // Optionally verify that user exists in DB
        assertTrue(accountRepository.findByEmail("test@example.com").isPresent());
    }

    @Test
    void login_success() throws Exception {
        AccountRequest request = AccountRequest.builder()
                .email("test@example.com")
                .password("password")
                .build();

        ObjectNode node = objectMapper.createObjectNode();
        node.put("access_token", "tokenmock");
        node.put("expires_in", 120);

        Mockito.when(keycloakAdminGateway.getToken(ArgumentMatchers.any())).thenReturn(node);

        mockMvc.perform(get("/account/login") //
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("login success"))
                .andExpect(jsonPath("$.token").value("tokenmock"))
                .andExpect(jsonPath("$.expire_in").value(120));
    }

}
