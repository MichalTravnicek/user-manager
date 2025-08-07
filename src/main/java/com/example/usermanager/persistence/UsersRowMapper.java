package com.example.usermanager.persistence;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.example.usermanager.model.User;

public class UsersRowMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet resultSet, int i) throws SQLException {
        int id = resultSet.getInt("id");

        String firstName = resultSet.getString("FIRST_NAME");
        String lastName = resultSet.getString("LAST_NAME");
        return new User(id, firstName, lastName);
    }
}