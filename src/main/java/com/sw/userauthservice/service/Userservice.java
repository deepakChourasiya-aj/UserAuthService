package com.sw.userauthservice.service;

import com.sw.userauthservice.models.User;
import com.sw.userauthservice.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class Userservice {

    @Autowired
    private UserRepository userRepository;

    public User getUserDetailsById(Long id){
        Optional<User> userOptional = userRepository.findById(id);
        if(userOptional.isEmpty()) return null;
        System.out.println(userOptional.get().getEmail()+" User Email");
        return userOptional.get();
    }
}
