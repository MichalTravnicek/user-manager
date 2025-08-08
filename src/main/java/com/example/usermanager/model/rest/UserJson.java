package com.example.usermanager.model.rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserJson {
    private String name;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String birthDate;
    private String registeredOn;
}
