package com.example.bankapps.model.dto;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClientRolesDto {
    private String id;            // UUID of the role
    private String name;          // Role name
    private String description;   // Optional description
    private boolean composite;    // Is it composite?
    private boolean clientRole;   // Is it a client role?
    private String containerId;   // UUID of the client containing the role
    private Map<String, Object> attributes; // Optional key-value attributes
}
