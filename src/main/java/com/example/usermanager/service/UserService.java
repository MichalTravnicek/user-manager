package com.example.usermanager.service;

import java.util.List;
import java.util.UUID;

import com.example.usermanager.model.rest.UserJson;

public interface UserService {

    UserJson createUser(UserJson userJson);

    @Deprecated
    UserJson getUser(String email);

    UserJson getUserById(UUID uuid);

    boolean updateUser(UserJson userJson);

    @Deprecated
    boolean deleteUser(String email);

    boolean deleteUserById(UUID uuid);

    List<UserJson> getAllUsers();

    List<UserJson> getUsersByParams(UserJson params);

}
