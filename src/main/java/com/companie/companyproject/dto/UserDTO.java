package com.companie.companyproject.dto;

import com.companie.companyproject.enums.UserRole;
import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String email;
    private String password;
    private String name;
    private String lastName;
    private String phone;
    private UserRole role;
}
