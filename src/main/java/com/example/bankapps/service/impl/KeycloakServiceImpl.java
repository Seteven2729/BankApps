package com.example.bankapps.service.impl;

import com.example.bankapps.gateway.KeycloakAdminGateway;
import com.example.bankapps.model.dto.ClientRolesDto;
import com.example.bankapps.model.dto.UserDto;
import com.example.bankapps.service.KeycloakService;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KeycloakServiceImpl implements KeycloakService {
    private final KeycloakAdminGateway keycloakAdminGateway;

    @Override
    public String getTokenAdmin() {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "client_credentials");
        formData.add("client_id", "bank-app-admin-clientid");
        formData.add("client_secret", "0vwSkNgKRd75wuWW4pVETAohlwsyTkDF");
        JsonNode responseNode = keycloakAdminGateway.getAdminToken(formData);
        return responseNode.path("access_token").asText();
    }

    @Override
    public void createUser(String token , UserDto userDto) {
        if (StringUtils.isEmpty(token)) {
            throw new IllegalStateException("Failed to get admin token from Keycloak");
        }
        keycloakAdminGateway.createUser( token, userDto);
        JsonNode responseClientId = keycloakAdminGateway.getClientId(token,"bank-apps-clientId");
        String clientUUID = responseClientId.get(0).path("id").asText();
        ClientRolesDto rolesDto = keycloakAdminGateway.getClientRoles(token,clientUUID,"user");
        String userId = getUserId(token,userDto.getUsername());
        keycloakAdminGateway.assignRole(token,clientUUID,userId, List.of(rolesDto));
    }

    @Override
    public String getUserId(String token , String username) {
          if (StringUtils.isEmpty(token)) {
            throw new IllegalStateException("Failed to get admin token from Keycloak");
        }
       JsonNode response =  keycloakAdminGateway.getUserId(  token,username);
        return response.get(0).path("id").asText();
    }

    @Override
    public void deleteUser(String token, String userId) {
        keycloakAdminGateway.deleteUser(  token,userId);
    }
}
