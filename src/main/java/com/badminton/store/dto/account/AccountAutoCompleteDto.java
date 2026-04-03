package com.badminton.store.dto.account;

import lombok.Data;

@Data
public class AccountAutoCompleteDto {
    private Long id;
    private String fullName;
    private String avatarPath;
}
