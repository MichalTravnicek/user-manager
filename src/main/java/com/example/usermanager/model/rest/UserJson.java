package com.example.usermanager.model.rest;

import org.hibernate.validator.constraints.UUID;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserJson {

    public static final String DATE_FORMAT = "yyyy-MM-dd";

    @UUID
    @Null(groups = CreateUser.Group.class)
    private String uuid;
    @NotNull
    @Size(min = 4, max = 8)
    private String name;
    @NotNull
    @Size(min = 2, max = 50)
    private String firstName;
    @NotNull
    @Size(min= 2, max = 50)
    private String lastName;
    @NotNull
    @Email
    private String emailAddress;
    @NotNull
    @DateConstraint(DATE_FORMAT)
    private String birthDate;
    @DateConstraint(DATE_FORMAT)
    private String registeredOn;

}
