package com.example.usermanager.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class User {

    @Id
    @Column("ID")
    private Long id = 0L;
    @Column("FIRST_NAME")
    private String firstName;
    @Column("LAST_NAME")
    private String lastName;
    @Column("BIRTH_DATE")
    private Date birthDate;
    @Column("REGISTERED_DATE")
    private Date registeredDate = getCurrentDate();

    public static SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

    private Date getCurrentDate(){
        try {
            return dateFormatter.parse(dateFormatter.format(new Date()));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public User(final long id, final String firstName, final String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public User() {

    }
}
