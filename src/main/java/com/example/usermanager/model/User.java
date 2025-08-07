package com.example.usermanager.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class User {

    @Id
    @Column("ID")
    private Long id = 0L;
    @Column("FIRST_NAME")
    private String firstName = "";
    @Column("LAST_NAME")
    private String lastName = "";

    public User(final long id, final String firstName, final String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public User() {

    }
}
