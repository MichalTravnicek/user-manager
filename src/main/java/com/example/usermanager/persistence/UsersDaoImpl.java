package com.example.usermanager.persistence;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.usermanager.model.User;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UsersDaoImpl implements UsersDao {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public User getOne(long id){
        return jdbcTemplate.queryForObject("SELECT * FROM USERS WHERE id= ?", new UsersRowMapper(), id);
    }

    @Override
    public User createOne(User user){
       throw new UnsupportedOperationException();
    }

    @Override
    public int updateOne(User user){
        throw new UnsupportedOperationException();
    }

    @Override
    public int deleteOne(User user){
        throw new UnsupportedOperationException();
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query("SELECT * FROM USERS", new UsersRowMapper());
    }
}
