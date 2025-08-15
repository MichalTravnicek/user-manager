package com.example.usermanager.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.usermanager.model.rest.UserJson;
import com.example.usermanager.service.UserService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@GetMapping("/")
    public List<UserJson> getAll() {
        //TODO returning all users is potential problem
        return userService.getAllUsers();
    }
}

