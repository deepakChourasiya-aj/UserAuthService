package com.sw.userauthservice.service;

import com.sw.userauthservice.exceptions.UserAlreadyExistsException;
import com.sw.userauthservice.exceptions.UserNotSignedUpException;
import com.sw.userauthservice.models.User;
import com.sw.userauthservice.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService implements IAuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public User signup(String name, String email, String password, String phoneNumber) {
        Optional<User> user = userRepository.findByEmail(email);

        if(user.isPresent()){
            throw new UserAlreadyExistsException("User already exists with these credential, please directly login!");
        }

        User newUser = new User();
        newUser.setName(name);
        newUser.setEmail(email);
        // Store encrypted password..
        newUser.setPassword(bCryptPasswordEncoder.encode(password));
        newUser.setPhoneNumber(phoneNumber);

        return userRepository.save(newUser);
    }

    @Override
    public User login(String email, String password) {
        // Check user exist with cred, make compare the pass then make the login..
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if(optionalUser.isEmpty()){
            throw new UserNotSignedUpException("Please create your account first!");
        }

        User user = optionalUser.get();
        if(!bCryptPasswordEncoder.matches(password,user.getPassword())){
            throw new UserNotSignedUpException("Wrong Password!");
        }
        // Else if match then make the user login..and assign jwt token ..

        Map<String,Object> claims = new HashMap<>();
        claims.put("userId",user.getId());
        claims.put("iss","akAndCompany");
        Long nowInMillis = System.currentTimeMillis();
        claims.put("gen",nowInMillis);
        claims.put("exp",nowInMillis+100000);
        claims.put("scope",user.getRolesList());

//        MacAlgorithem


        return null;
    }

    @Override
    public Boolean validateToken(String token, Long userId) {
        return null;
    }
}
