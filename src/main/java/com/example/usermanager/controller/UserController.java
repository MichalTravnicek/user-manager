package com.example.usermanager.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.usermanager.model.rest.UserJson;
import com.example.usermanager.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(UserController.BASE_URL)
public class UserController {

    public static final String BASE_URL = "/api/v1/users";

	private final UserService userService;

	@GetMapping("/")
    @Operation(tags = "1 - Get",
            summary = "Get all users",
            description = "Retrieves all users"
    )
    @ApiResponse(responseCode = "200", description = "Returns list of users", content = @Content)
    public List<UserJson> getAll() {
        //TODO returning all users is potential problem
        return userService.getAllUsers();
    }
}

