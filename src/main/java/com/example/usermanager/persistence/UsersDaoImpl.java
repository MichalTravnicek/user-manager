package com.example.usermanager.persistence;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.example.usermanager.model.User;
import com.example.usermanager.persistence.exception.ExistingUserConflict;
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
    public User getOneByEmail(final String email) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM USERS WHERE email= ?", new UsersRowMapper(), email);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException(e);
        }
    }

    @Override
    public User getOneByUuid(final UUID uuid) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM USERS WHERE uuid= ?", new UsersRowMapper(), uuid);
        } catch (EmptyResultDataAccessException ex) {
            throw new NotFoundException(ex);
        }
    }

    @Override
    public List<User> searchByFuzzy(Map<String,String> parameters) {
        final String columns = parameters.keySet().stream().map(x -> x + "::text LIKE ?")
                .collect(Collectors.joining(" AND "));
        List<Object> values = new ArrayList<>(parameters.values());
        try {
            return jdbcTemplate.query("SELECT * FROM USERS WHERE " + columns, new UsersRowMapper(), values.toArray());
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException(e);
        }
    }

    @Override
    public User createOne(User user){
        KeyHolder keyHolder = new GeneratedKeyHolder();
        final Map<String, Object> stringObjectMap = UsersRowMapper.mapToDatabase(user);
        final String columns = String.join(",", stringObjectMap.keySet());
        List<Object> values = new ArrayList<>(stringObjectMap.values().stream().toList());
        String placeholders = columns.replaceAll("\\w+","?");
        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection
                        .prepareStatement("INSERT INTO USERS (" + columns + ") VALUES (" + placeholders + ")",
                                Statement.RETURN_GENERATED_KEYS);
                for (int i = 1; i <= values.size(); i++) {
                    final Object value = values.get(i - 1);
                    if (value instanceof Date){
                        ps.setDate(i, new java.sql.Date(((Date) value).getTime()));
                    }
                    else {
                        ps.setObject(i, value);
                    }
                }
                return ps;
            }, keyHolder);
        } catch (DuplicateKeyException e) {
            throw new ExistingUserConflict(e.getMessage());
        }

        final Map<String, Object> keys = keyHolder.getKeys();
        return UsersRowMapper.addUserIds(user, keys);
    }

    @Override
    public int updateOne(User user){
        final Map<String, Object> stringObjectMap = UsersRowMapper.mapToDatabase(user);
        final String columns = String.join("=?,", stringObjectMap.keySet()) + "=?";
        List<Object> values = new ArrayList<>(stringObjectMap.values().stream().toList());
        values.add(user.getUuid());
        try {
            return jdbcTemplate.update("UPDATE USERS SET " + columns + " WHERE uuid=?", values.toArray());
        } catch (DuplicateKeyException e) {
            throw new ExistingUserConflict(e.getMessage());
        }
    }

    @Override
    public int deleteOne(User user){
        return jdbcTemplate.update("DELETE FROM USERS WHERE id= ?", user.getId());
    }

    @Override
    public int deleteOne(String email){
        return jdbcTemplate.update("DELETE FROM USERS WHERE email= ?", email);
    }

    @Override
    public int deleteOneById(UUID uuid){
        return jdbcTemplate.update("DELETE FROM USERS WHERE uuid= ?", uuid);
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
