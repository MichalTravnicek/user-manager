package com.example.usermanager.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class User {

    private long id;
    private String firstName;
    private String lastName;

    public User(final long id, final String firstName, final String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public User() {

    }
}
