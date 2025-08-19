package com.example.usermanager.persistence;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.example.usermanager.model.User;

public interface UsersDao {

    User getOne(long id);

    @Deprecated
    User getOneByEmail(String email);

    User getOneByUuid(UUID uuid);

    List<User> searchByFuzzy(Map<String,String> parameters);

    User createOne(User user);

    int updateOne(User user);

    int deleteOne(User user);

    @Deprecated
    int deleteOne(String email);

    int deleteOneById(UUID uuid);

    List<User> getAll();

    Long count();
}
