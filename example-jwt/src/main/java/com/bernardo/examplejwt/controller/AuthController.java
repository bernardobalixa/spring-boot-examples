package com.bernardo.examplejwt.controller;

import com.bernardo.examplejwt.dto.LoginDTO;
import com.bernardo.examplejwt.dto.RegisterDTO;
import com.bernardo.examplejwt.entity.Role;
import com.bernardo.examplejwt.entity.UserEntity;
import com.bernardo.examplejwt.repository.RoleRepository;
import com.bernardo.examplejwt.repository.UserRepository;
import com.bernardo.examplejwt.security.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private AuthenticationManager authenticationManager;

    private UserRepository userRepository;

    private RoleRepository roleRepository;

    private PasswordEncoder passwordEncoder;

    private JwtProvider jwtProvider;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, JwtProvider jwtProvider) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDTO loginDTO) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDTO.getUsername(),
                            loginDTO.getPassword()));

            String token = jwtProvider.generateToken(authentication);

            return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, token).body("Logged in!");
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterDTO registerDTO) {
        if (userRepository.existsByUsername(registerDTO.getUsername())) {
            return ResponseEntity.badRequest().body("Username is taken.");
        }

        UserEntity user = new UserEntity();

        user.setUsername(registerDTO.getUsername());
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));

        Optional<Role> roleOptional = roleRepository.findByName("USER");

        if (roleOptional.isEmpty()){
            return ResponseEntity.internalServerError().body("Role does not exist!");
        }

        Role role = roleOptional.get();

        user.setRoles(Collections.singletonList(role));

        userRepository.save(user);

        return ResponseEntity.ok().body("User created!");
    }

    @PostMapping("/registerAdmin")
    public ResponseEntity<String> registerAdmin(@RequestBody RegisterDTO registerDTO) {
        if (userRepository.existsByUsername(registerDTO.getUsername())) {
            return ResponseEntity.badRequest().body("Username is taken.");
        }

        UserEntity user = new UserEntity();

        user.setUsername(registerDTO.getUsername());
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));

        Optional<Role> roleOptional = roleRepository.findByName("ADMIN");

        if (roleOptional.isEmpty()){
            return ResponseEntity.internalServerError().body("Role does not exist!");
        }

        Role role = roleOptional.get();

        user.setRoles(Collections.singletonList(role));

        userRepository.save(user);

        return ResponseEntity.ok().body("Admin created!");
    }
}
