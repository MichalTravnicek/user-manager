package com.example.usermanager.persistence;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.example.usermanager.model.User;
import com.example.usermanager.persistence.exception.NotFoundException;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UsersDaoImpl implements UsersDao {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public User getOne(long id){
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM USERS WHERE id= ?", new UsersRowMapper(), id);
        }catch (EmptyResultDataAccessException ex){
            throw new NotFoundException(ex);
        }
    }

    @Override
    public User createOne(User user){
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement("INSERT INTO USERS (FIRST_NAME, LAST_NAME) VALUES (?, ?)",
                            Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getFirstName());
            ps.setString(2, user.getLastName());
            return ps;
        }, keyHolder);

        long id = keyHolder.getKey().longValue();
        user.setId(id);
        return user;
    }

    @Override
    public int updateOne(User user){
        return jdbcTemplate.update("UPDATE USERS SET FIRST_NAME=?,LAST_NAME=? WHERE id= ?",
                user.getFirstName(), user.getLastName(), user.getId());
    }

    @Override
    public int deleteOne(User user){
        return jdbcTemplate.update("DELETE FROM USERS WHERE id= ?", user.getId());
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query("SELECT * FROM USERS", new UsersRowMapper());
    }
}
