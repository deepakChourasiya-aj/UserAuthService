package com.sw.userauthservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sw.userauthservice.KafkaProducerClient.KafkaProducerClient;
import com.sw.userauthservice.dtos.EmailDto;
import com.sw.userauthservice.exceptions.UserAlreadyExistsException;
import com.sw.userauthservice.exceptions.UserNotSignedUpException;
import com.sw.userauthservice.models.Session;
import com.sw.userauthservice.models.SessionState;
import com.sw.userauthservice.models.User;
import com.sw.userauthservice.repositories.SessionRepository;
import com.sw.userauthservice.repositories.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.MacAlgorithm;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService implements IAuthService {

    @Autowired
    private KafkaProducerClient kafkaProducerClient;

    @Autowired
    public ObjectMapper objectMapper;


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private SecretKey secretKey;

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

        // Send welcome email.
        try{
            EmailDto emailDto = new EmailDto();
            emailDto.setSubject("Welcome to Dx Fashion Limited");
            emailDto.setBody("Have a good fashional experience");
            emailDto.setFrom(email);
            String message = objectMapper.writeValueAsString(emailDto);
            kafkaProducerClient.sendMessage("signup",message);
        }catch (JsonProcessingException exception){
            throw new RuntimeException(exception.getMessage());
        }

        return userRepository.save(newUser);
    }

    @Override
    public Pair<User,String> login(String email, String password) {
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

        String token = Jwts.builder().claims(claims).signWith(secretKey).compact();

        // Create the new session..
        Session session = new Session();
        session.setUser(user);
        session.setToken(token);
        session.setState(SessionState.ACTIVE);
        sessionRepository.save(session);

        return new Pair<User,String>(user,token);
    }

    @Override
    public Boolean validateToken(String token, Long userId) {
        Optional<Session> optionalSession = sessionRepository.findByTokenAndUser_Id(token,userId);

        if(optionalSession.isEmpty()){
            return false;
        }
        Session session = optionalSession.get();

        JwtParser jwtParser = Jwts.parser().verifyWith(secretKey).build();
        Claims claims = jwtParser.parseSignedClaims(token).getPayload();

        Long expiry = (Long) claims.get("exp");
        Long currentTime = System.currentTimeMillis();

        if(expiry < currentTime){
            session.setState(SessionState.EXPIRED);
            sessionRepository.save(session);
            return false;
        }
        return true;
    }
}
