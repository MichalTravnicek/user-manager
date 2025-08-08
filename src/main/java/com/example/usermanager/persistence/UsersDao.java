package com.example.usermanager.persistence;

import java.util.List;

import com.example.usermanager.model.User;

public interface UsersDao {

    User getOne(long id);

    User getOneByEmail(String email);

    User createOne(User user);

    int updateOne(User user);

    int deleteOne(User user);

    int deleteOne(String email);

    List<User> getAll();

    Long count();
}
