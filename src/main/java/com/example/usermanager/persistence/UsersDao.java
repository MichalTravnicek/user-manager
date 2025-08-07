package com.example.usermanager.persistence;

import java.util.List;

import com.example.usermanager.model.User;

public interface UsersDao {
    User getOne(long id);

    User createOne(User user);

    int updateOne(User user);

    int deleteOne(User user);

    List<User> getAll();

    Long count();
}
