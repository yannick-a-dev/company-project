package com.companie.companyproject.dto;

import lombok.Data;

@Data
public class AuthenticationRequest {
    private String userName;
    private String password;
}
