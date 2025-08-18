package com.example.usermanager.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.usermanager.model.rest.CreateUser;
import com.example.usermanager.model.rest.UserJson;
import com.example.usermanager.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
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

    @GetMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found",
                    content = @Content(schema = @Schema(example = """
                            {
                              "uuid": "b1b44b12-34bc-4ed7-a666-9657b8b8c31b",
                              "name": "mtrava",
                              "firstName": "Michal",
                              "lastName": "Trava",
                              "emailAddress": "trava@seznam.cz",
                              "birthDate": "1972-08-25",
                              "registeredOn": "2025-05-07"
                            }
                            """))),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content)
    })
    @Operation(tags = "1 - Get",
            summary = "Get user by UUID",
            description = "Retrieves a user by unique identifier"
    )
    public ResponseEntity<UserJson> getUser(
            @Parameter(examples = {
                    @ExampleObject(name = "Existing user UUID", description = "Returns Josh user", value = "deccc52a-4c7d-4f0c-9ba9-e12b6dd3c381"),
                    @ExampleObject(name = "Nonexistent user UUID", description = "Results in 404 NotFound", value = "b1b44b12-34bc-4ed7-a666-9657b8b8c31b"),
                    @ExampleObject(name = "Invalid UUID", description = "Results in 400 BadRequest", value = "b1b44b12-34bc")
            })
            @RequestParam @org.hibernate.validator.constraints.UUID String uuid) {
        UUID uuidObj = UUID.fromString(uuid);
        return ResponseEntity.ok(userService.getUserById(uuidObj));
    }

    @PostMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(tags = "2 - Create",
            summary = "Create user",
            description = "Creates user from supplied values"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created",
                    content = @Content(schema = @Schema(example = """
                            {
                              "uuid": "b1b44b12-34bc-4ed7-a666-9657b8b8c31b",
                              "name": "mtrava",
                              "firstName": "Michal",
                              "lastName": "Trava",
                              "emailAddress": "trava@seznam.cz",
                              "birthDate": "1972-08-25",
                              "registeredOn": "2025-05-07"
                            }
                            """))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "409", description = "Conflict with existing user", content = @Content)
    })
    public ResponseEntity<UserJson> createUser(
            @Schema(implementation = UserJson.class, example = """
                            {
                              "name": "mtrava",
                              "firstName": "Michal",
                              "lastName": "Trava",
                              "emailAddress": "trava@seznam.cz",
                              "birthDate": "1972-08-25",
                              "registeredOn": "2025-05-07"
                            }
                            """)
            @RequestBody @Validated(CreateUser.class) UserJson user) {
        final UserJson createdUser = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PutMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(tags = "3 - Update",
            summary = "Update user",
            description = "Updates user from supplied values"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User updated", content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad request/Not found", content = @Content),
            @ApiResponse(responseCode = "409", description = "Conflict with existing user", content = @Content)
    })
    public ResponseEntity<UserJson> updateUser(
            @Schema(implementation = UserJson.class, example = """
                            {
                              "uuid": "f6e4208f-5df4-466e-9225-01f296e2a09c",
                              "name": "pbobek",
                              "firstName": "Pavel",
                              "lastName": "Bobek",
                              "emailAddress": "pbob@seznam.cz",
                              "birthDate": "1971-08-20",
                              "registeredOn": "2024-08-07"
                            }
                            """)
            @RequestBody @Valid UserJson user) {
        boolean updateResult = userService.updateUser(user);
        if (updateResult) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

    }

    @Operation(tags = "4 - Delete",
            summary = "Delete user",
            description = "Deletes user by id"
    )
    @DeleteMapping("/user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User deleted", content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content),

    })
    public ResponseEntity<Void> deleteUser(
            @Parameter(examples = {
                    @ExampleObject(name = "Existing user UUID", description = "Deletes Josh user", value = "deccc52a-4c7d-4f0c-9ba9-e12b6dd3c381"),
                    @ExampleObject(name = "Nonexistent user UUID", description = "Results in 404 NotFound", value = "b1b44b12-34bc-4ed7-a666-9657b8b8c31b"),
                    @ExampleObject(name = "Invalid UUID", description = "Results in 400 BadRequest", value = "b1b44b12-34bc")
            }
            )
            @RequestParam @org.hibernate.validator.constraints.UUID String uuid
    ){
        UUID uuidObj = UUID.fromString(uuid);
        final boolean deleted = userService.deleteUserById(uuidObj);
        if (deleted) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}
