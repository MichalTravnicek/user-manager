package com.example.usermanager.persistence;

import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
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
       throw new UnsupportedOperationException();
    }

    @Override
    public int updateOne(User user){
        throw new UnsupportedOperationException();
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
