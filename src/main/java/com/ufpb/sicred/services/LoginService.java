package com.ufpb.sicred.services;

import com.ufpb.sicred.authentication.AuthenticationService;
import com.ufpb.sicred.dto.LoginDto;
import com.ufpb.sicred.dto.TokenDto;
import com.ufpb.sicred.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private AuthenticationService authenticationService;
    private JwtUtil jwtUtil;
    private AuthenticationManager authenticationManager;

    public LoginService(AuthenticationService authenticationService, JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.authenticationService = authenticationService;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    public TokenDto login(LoginDto dto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getUsuario(), dto.getSenha())
        );

        UserDetails userDetails = authenticationService.loadUserByUsername(dto.getUsuario());
        return new TokenDto(jwtUtil.generateToken(userDetails));
    }
}
