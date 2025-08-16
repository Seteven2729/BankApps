package com.example.bankapps.gateway;

import com.example.bankapps.model.dto.ClientRolesDto;
import com.example.bankapps.model.dto.UserDto;
import com.fasterxml.jackson.databind.JsonNode;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "KeycloakAdminGateway", url = "${app.sso.url}")
public interface KeycloakAdminGateway {

    @PostMapping(
            value = "/realms/master/protocol/openid-connect/token",
            consumes = "application/x-www-form-urlencoded"
    )
    @Headers("Content-Type: application/x-www-form-urlencoded")
    JsonNode getAdminToken(
            @RequestBody MultiValueMap<String, String> formData
    );

    @PostMapping(
            value = "/realms/master/protocol/openid-connect/token",
            consumes = "application/x-www-form-urlencoded"
    )
    @Headers("Content-Type: application/x-www-form-urlencoded")
    JsonNode getToken(
            @RequestBody MultiValueMap<String, String> formData
    );

    @PostMapping(value = "/admin/realms/master/users", consumes = "application/json")
    void createUser(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String token, @RequestBody UserDto user);

    @DeleteMapping(value = "/admin/realms/master/users/{userId}")
    void deleteUser(@RequestHeader("Authorization") String token,
                    @PathVariable("userId") String userId);

    @GetMapping(value = "admin/realms/master/users")
    JsonNode getUserId(@RequestHeader("Authorization") String token,
                    @RequestParam("username") String username);

    @GetMapping(value = "/admin/realms/master/clients")
    JsonNode getClientId(@RequestHeader("Authorization") String token,
                         @RequestParam("clientId") String clientId);

    @GetMapping(value = "/admin/realms/master/clients/{clientUUID}/roles/{roleName}")
    ClientRolesDto getClientRoles(@RequestHeader("Authorization") String token,
                                  @PathVariable("clientUUID") String clientUUID,
                                  @PathVariable("roleName") String roleName);

    @PostMapping(value = "/admin/realms/master/users/{userId}/role-mappings/clients/{clientUUID}")
    void assignRole(@RequestHeader("Authorization") String token,
                    @PathVariable("clientUUID") String clientUUID,
                    @PathVariable("userId") String userId,
                    @RequestBody List<ClientRolesDto> dtos);

}
