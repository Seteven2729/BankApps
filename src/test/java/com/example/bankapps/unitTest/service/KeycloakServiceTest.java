package com.example.bankapps.unitTest.service;

import com.example.bankapps.gateway.KeycloakAdminGateway;
import com.example.bankapps.model.dto.*;
import com.example.bankapps.service.impl.KeycloakServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class KeycloakServiceTest {

    @Mock
    private KeycloakAdminGateway keycloakAdminGateway;

    @InjectMocks
    private KeycloakServiceImpl keycloakService;

    private ObjectMapper mapper;

    @BeforeEach
    void setup() {
        mapper = new ObjectMapper();
    }

    @Test
    void successGetTokenAdmin() {

        ObjectNode node = mapper.createObjectNode();
        node.put("access_token", "tokenmock");

        Mockito.when(keycloakAdminGateway.getAdminToken(ArgumentMatchers.any())).thenReturn(node);
        String token = keycloakService.getTokenAdmin();

        assertNotNull(token, "token cant be null");
        assertFalse(token.isEmpty(), "token cant be empty");

        Mockito.verify(keycloakAdminGateway).getAdminToken(ArgumentMatchers.any());

    }


    @Test
    void successGetToken() {

        ObjectNode node = mapper.createObjectNode();
        node.put("access_token", "tokenmock");
        node.put("expires_in", 120);

        Mockito.when(keycloakAdminGateway.getToken(ArgumentMatchers.any())).thenReturn(node);
        TokenDto token = keycloakService.getToken(AccountRequest.builder()
                .email("mock@gmail.com")
                .password("password")
                .build());

        assertNotNull(token, "token cant be null");
        assertFalse(token.getToken().isEmpty(), "token cant be empty");

        Mockito.verify(keycloakAdminGateway).getToken(ArgumentMatchers.any());

    }

    @Test
    void successCreateUser() {

        ObjectNode node = mapper.createObjectNode();
        node.put("id", "asdasd");
        ArrayNode arrayNode = mapper.createArrayNode();
        arrayNode.add(node);

        Mockito.doNothing().when(keycloakAdminGateway).createUser(Mockito.anyString(), Mockito.any(UserDto.class));
        Mockito.when(keycloakAdminGateway.getClientId(Mockito.anyString(), Mockito.anyString())).thenReturn(arrayNode);
        Mockito.when(keycloakAdminGateway.getClientRoles(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(new ClientRolesDto());
        Mockito.when(keycloakAdminGateway.getUserId(Mockito.anyString(), Mockito.anyString())).thenReturn(arrayNode);
        Mockito.doNothing().when(keycloakAdminGateway).assignRole(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyList());

        UserDto userDto = UserDto.builder()
                .email("email")
                .username("email")
                .enabled(false)
                .credentials(List.of(CredentialDto.builder()
                        .value("value")
                        .build()))
                .build();

        keycloakService.createUser("token", userDto);

        Mockito.verify(keycloakAdminGateway).createUser(Mockito.anyString(), Mockito.any(UserDto.class));
        Mockito.verify(keycloakAdminGateway).getClientId(Mockito.anyString(), Mockito.anyString());
        Mockito.verify(keycloakAdminGateway).getClientRoles(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
        Mockito.verify(keycloakAdminGateway).getUserId(Mockito.anyString(), Mockito.anyString());
        Mockito.verify(keycloakAdminGateway).assignRole(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyList());

    }

    @Test
    void errorCreateUser() {

        UserDto userDto = UserDto.builder()
                .email("email")
                .username("email")
                .enabled(false)
                .credentials(List.of(CredentialDto.builder()
                        .value("value")
                        .build()))
                .build();

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> {
            keycloakService.createUser("", userDto);
        });

        assertEquals("Failed to get admin token from Keycloak", ex.getMessage());

    }

    @Test
    void errorGetUserId() {
        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> {
            keycloakService.getUserId("", "user");
        });
        assertEquals("Failed to get admin token from Keycloak", ex.getMessage());

    }

    @Test
    void successDeleteUser(){
        Mockito.doNothing().when(keycloakAdminGateway).deleteUser(Mockito.anyString(),Mockito.anyString());
        keycloakService.deleteUser("token","user");
        Mockito.verify(keycloakAdminGateway).deleteUser(Mockito.anyString(),Mockito.anyString());
    }

    @Test
    void errorDeleteUser() {
        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> {
            keycloakService.deleteUser("", "user");
        });
        assertEquals("Failed to get admin token from Keycloak", ex.getMessage());

    }


}
