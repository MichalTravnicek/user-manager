package com.example.usermanager.persistence;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

import com.example.usermanager.model.Column;
import com.example.usermanager.model.Id;
import com.example.usermanager.model.User;

public class UsersRowMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet resultSet, int row) throws SQLException {

        Map<String, Object> results = new HashMap<>();
        int columnCount = resultSet.getMetaData().getColumnCount();
        for (int i=1; i<=columnCount; i++)
        {
            String columnName = resultSet.getMetaData().getColumnName(i);
            results.put(columnName.toUpperCase(), resultSet.getObject(i));
        }

        User user = new User();

        for (final Field field : User.class.getDeclaredFields()) {
            if (field.isAnnotationPresent(Column.class)) {
                final Column annotation = field.getAnnotation(Column.class);
                Object value = results.get(annotation.value().toUpperCase());
                field.setAccessible(true);
                try {
                    if (field.getType().equals(Long.class)){
                        field.set(user, ((Number) value).longValue());
                    }
                    else {
                        field.set(user, value);
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return user;
    }

    public Map<String, Object> mapToDatabase(User user){
        Map<String,Object> columns = new LinkedHashMap<>();

        for (final Field field : User.class.getDeclaredFields()) {
            if (field.isAnnotationPresent(Column.class)) {
                final Column annotation = field.getAnnotation(Column.class);
                try {
                    if (field.isAnnotationPresent(Id.class)){
                        continue;
                    }
                    field.setAccessible(true);
                    columns.put(annotation.value(), field.get(user));
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return columns;

    }
}