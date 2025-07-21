package com.sw.userauthservice.models;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class User extends BaseModel{
    private String name;
    private String email;
    private String password;
    private String phoneNumber;

    @ManyToMany
    private List<Role> rolesList = new ArrayList<>();
}

// 1 USER CAN HAVE MANY ROLES
// 1 ROLE CAN WE ASSIGNED TO MULTIPLE USERS..
// SO ===> M : M
