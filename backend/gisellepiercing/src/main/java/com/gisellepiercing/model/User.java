package com.gisellepiercing.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String cpf;
    private String role;
}