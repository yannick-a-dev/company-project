package com.companie.companyproject.controller;

import com.companie.companyproject.dto.AuthenticationRequest;
import com.companie.companyproject.dto.SignupRequestDTO;
import com.companie.companyproject.dto.UserDTO;
import com.companie.companyproject.model.User;
import com.companie.companyproject.repository.UserRepo;
import com.companie.companyproject.service.authentication.AuthService;
import com.companie.companyproject.service.jwt.UserDetailsServiceImpl;
import com.companie.companyproject.utils.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class AuthenticationController {
    @Autowired
    private AuthService authService;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private AuthenticationManager authenticationManager;
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";

    @PostMapping("/client/signup")
    public ResponseEntity<?> signupClient(@RequestBody SignupRequestDTO signupRequestDTO){
        if(authService.presentByEmail(signupRequestDTO.getEmail())){
            return new ResponseEntity<>("Client already exists with this Email", HttpStatus.NOT_ACCEPTABLE);
        }
        UserDTO createdUser = authService.signupCLient(signupRequestDTO);
        return new ResponseEntity<>(createdUser,HttpStatus.OK);
    }

    @PostMapping("/company/signup")
    public ResponseEntity<?> signupCompany(@RequestBody SignupRequestDTO signupRequestDTO){
        if(authService.presentByEmail(signupRequestDTO.getEmail())){
            return new ResponseEntity<>("Company already exists with this Email", HttpStatus.NOT_ACCEPTABLE);
        }
        UserDTO createdUser = authService.signupCompany(signupRequestDTO);
        return new ResponseEntity<>(createdUser,HttpStatus.OK);
    }
    @PostMapping("/authenticate")
    public void createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest,
                                          HttpServletResponse response) throws IOException, JSONException {
     try {
       authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
           authenticationRequest.getUserName(),authenticationRequest.getPassword()
       ));
     }catch (BadCredentialsException e){
         throw  new BadCredentialsException("Incorrect userName or password",e);
     }
     final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUserName());

     final String jwt = jwtUtil.generateToken((userDetails.getUsername()));
        User user = userRepo.findFirstByEmail(authenticationRequest.getUserName());

        response.getWriter().write(new JSONObject()
                .put("userId",user.getId())
                .put("role",user.getRole())
                .toString()
        );

        response.addHeader("Access-Control-Expose-Headers", "Authorization");
        response.addHeader("Access-Control-Allow-Headers", "Authorization," +
                "X-PINGOTHER, Origin, X-Requested-With, Content-Type, Accept, X-Custom-header");

        response.addHeader(HEADER_STRING, TOKEN_PREFIX + jwt);
    }
}
