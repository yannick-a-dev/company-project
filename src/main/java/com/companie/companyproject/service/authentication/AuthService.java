package com.companie.companyproject.service.authentication;

import com.companie.companyproject.dto.SignupRequestDTO;
import com.companie.companyproject.dto.UserDTO;

public interface AuthService {
    UserDTO signupCLient(SignupRequestDTO signupRequestDTO);
    public UserDTO signupCompany(SignupRequestDTO signupRequestDTO);
    public Boolean presentByEmail(String email);
}
