package com.sw.userauthservice.controllers;

import com.sw.userauthservice.dtos.LoginRequestDto;
import com.sw.userauthservice.dtos.SignupRequestDto;
import com.sw.userauthservice.dtos.UserDto;
import com.sw.userauthservice.dtos.ValidateTokenRequestDto;
import com.sw.userauthservice.models.User;
import com.sw.userauthservice.service.IAuthService;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @Autowired
    private IAuthService authService;


    @PostMapping("/signup")
    public ResponseEntity<UserDto> signup(@RequestBody SignupRequestDto signupRequestDto){
        // Take his details and check if already there return already exist, else signup
        User user =  authService.signup(signupRequestDto.getName(),signupRequestDto.getEmail(),signupRequestDto.getPassword(),signupRequestDto.getPhoneNumber());
        UserDto userDto = from(user);
        return new ResponseEntity<>(userDto,HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public Pair<User,String> login(@RequestBody LoginRequestDto loginRequestDto){
        Pair<User,String> response = authService.login(loginRequestDto.getEmail(),loginRequestDto.getPassword());
        return null;
    }

    @PostMapping("/validateToken")
    public Boolean validateToken(@RequestBody ValidateTokenRequestDto validateTokenRequestDto){
        return null;
    }

    UserDto from(User user){
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        return userDto;
    }
}
