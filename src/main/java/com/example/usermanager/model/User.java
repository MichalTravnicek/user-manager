package com.example.usermanager.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class User {

    @Id
    @Column("ID")
    private Long id = 0L;
    @Id
    @Column("UUID")
    private UUID uuid;
    @Column("NAME")
    private String name;
    @Column("FIRST_NAME")
    private String firstName;
    @Column("LAST_NAME")
    private String lastName;
    @Column("EMAIL")
    private String email;
    @Column("BIRTH_DATE")
    private Date birthDate;
    @Column("REGISTERED_DATE")
    private Date registeredDate = getCurrentDate();

    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static SimpleDateFormat dateFormatter = new SimpleDateFormat(DATE_FORMAT);

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
