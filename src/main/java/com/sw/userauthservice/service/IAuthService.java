package com.sw.userauthservice.service;

import com.sw.userauthservice.models.User;

public interface IAuthService {
    User signup(String name,String email,String password,String phoneNumber);
    User login(String email,String password);
    Boolean validateToken(String token,Long userId);
}
