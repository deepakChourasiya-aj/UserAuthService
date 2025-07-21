package com.sw.userauthservice.controllers;

import com.sw.userauthservice.dtos.LoginRequestDto;
import com.sw.userauthservice.dtos.SignupRequestDto;
import com.sw.userauthservice.dtos.UserDto;
import com.sw.userauthservice.dtos.ValidateTokenRequestDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    // signup
    // login
    // validateToken
    // logout
    // forgetPassword

    @PostMapping("/signup")
    public UserDto signup(@RequestBody SignupRequestDto signupRequestDto){
        return null;
    }

    @PostMapping("/login")
    public UserDto login(@RequestBody LoginRequestDto loginRequestDto){
        return null;
    }

    @PostMapping("/validateToken")
    public Boolean validateToken(@RequestBody ValidateTokenRequestDto validateTokenRequestDto){
        return null;
    }

}
