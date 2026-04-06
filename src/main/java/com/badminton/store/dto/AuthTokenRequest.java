package com.badminton.store.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthTokenRequest {
    private String grant_type;
    private String email;
    private String client_id;
    private String client_secret;
}