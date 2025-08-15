package com.sw.userauthservice.controllers;

import com.sw.userauthservice.dtos.UserDto;
import com.sw.userauthservice.models.User;
import com.sw.userauthservice.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;


@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/{id}")
    public UserDto getUserDetailsById(@PathVariable Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if(user.isPresent()) return from(user.get());
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
