package com.badminton.store.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokenRequest {
    private String grant_type;
    private String email;
    private String client_id;
    private String client_secret;
}