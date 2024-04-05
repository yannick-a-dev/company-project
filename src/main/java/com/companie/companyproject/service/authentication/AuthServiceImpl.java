package com.companie.companyproject.service.authentication;

import com.companie.companyproject.dto.SignupRequestDTO;
import com.companie.companyproject.dto.UserDTO;
import com.companie.companyproject.enums.UserRole;
import com.companie.companyproject.model.User;
import com.companie.companyproject.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService{
    @Autowired
    private UserRepo userRepo;

    public UserDTO signupCLient(SignupRequestDTO signupRequestDTO){
        User user = new User();
        user.setName(signupRequestDTO.getName());
        user.setLastName(signupRequestDTO.getLastName());
        user.setEmail(signupRequestDTO.getEmail());
        user.setPhone(signupRequestDTO.getPhone());
        user.setPassword(new BCryptPasswordEncoder().encode(signupRequestDTO.getPassword()));
        user.setRole(UserRole.CLIENT);
        return userRepo.save(user).getDTO();
    }

    public Boolean presentByEmail(String email){
        return userRepo.findFirstByEmail(email) !=null;
    }

    public UserDTO signupCompany(SignupRequestDTO signupRequestDTO){
        User user = new User();
        user.setName(signupRequestDTO.getName());
        user.setEmail(signupRequestDTO.getEmail());
        user.setPhone(signupRequestDTO.getPhone());
        user.setPassword(new BCryptPasswordEncoder().encode(signupRequestDTO.getPassword()));
        user.setRole(UserRole.COMPANY);
        return userRepo.save(user).getDTO();
    }
}
