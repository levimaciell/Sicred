package com.ufpb.sicred.controllers;

import com.ufpb.sicred.dto.LoginDto;
import com.ufpb.sicred.dto.TokenDto;
import com.ufpb.sicred.services.LoginService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AuthenticationController {

    private LoginService loginService;

    public AuthenticationController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping(path = "/login")
    public TokenDto login(@RequestBody LoginDto dto){
        return loginService.login(dto);
    }
}
