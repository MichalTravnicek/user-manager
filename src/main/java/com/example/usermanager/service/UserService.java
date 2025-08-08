package com.example.usermanager.service;

import java.util.List;

import com.example.usermanager.model.rest.UserJson;

public interface UserService {

    boolean createUser(UserJson userJson);

    UserJson getUser(String email);

    boolean updateUser(UserJson userJson);

    boolean deleteUser(String email);

    List<UserJson> getAllUsers();
}
