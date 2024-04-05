package com.companie.companyproject.model;

import com.companie.companyproject.dto.UserDTO;
import com.companie.companyproject.enums.UserRole;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String password;
    private String name;
    private String lastName;
    private String phone;
    private UserRole role;

    public UserDTO getDTO(){
        UserDTO userDTO = new UserDTO();
        userDTO.setId(id);
        userDTO.setName(name);
        userDTO.setEmail(email);
        userDTO.setRole(role);
        return userDTO;
    }
}
