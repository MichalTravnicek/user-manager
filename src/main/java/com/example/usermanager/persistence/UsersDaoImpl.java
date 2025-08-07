package com.example.usermanager.persistence;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        final Map<String, Object> stringObjectMap = new UsersRowMapper().mapToDatabase(user);
        final String columns = String.join(",", stringObjectMap.keySet());
        List<Object> values = new ArrayList<>(stringObjectMap.values().stream().toList());
        String placeholders = columns.replaceAll("\\w+","?");
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement("INSERT INTO USERS (" + columns + ") VALUES (" + placeholders + ")",
                            Statement.RETURN_GENERATED_KEYS);
            for (int i = 1; i <= values.size(); i++) {
                ps.setObject(i, values.get(i-1));
            }
            return ps;
        }, keyHolder);

        long id = keyHolder.getKey().longValue();
        user.setId(id);
        return user;
    }

    @Override
    public int updateOne(User user){
        final Map<String, Object> stringObjectMap = new UsersRowMapper().mapToDatabase(user);
        final String columns = String.join("=?,", stringObjectMap.keySet()) + "=?";
        List<Object> values = new ArrayList<>(stringObjectMap.values().stream().toList());
        values.add(user.getId());
        return jdbcTemplate.update("UPDATE USERS SET " + columns + " WHERE id=?", values.toArray());
    }

    @Override
    public int deleteOne(User user){
        return jdbcTemplate.update("DELETE FROM USERS WHERE id= ?", user.getId());
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query("SELECT * FROM USERS", new UsersRowMapper());
    }

    @Override
    public Long count() {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM USERS", Long.class);
    }

}
