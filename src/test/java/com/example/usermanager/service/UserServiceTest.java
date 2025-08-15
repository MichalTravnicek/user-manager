package com.example.usermanager.service;

import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;

import com.example.usermanager.model.rest.UserJson;
import com.example.usermanager.persistence.exception.NotFoundException;

import static org.junit.jupiter.api.Assertions.assertThrows;

@JdbcTest
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
class UserServiceTest {

    @Autowired
    UserService userService;

    @Test
    public void shouldCreateUser() {
        UserJson userJson = new UserJson(null, "jtluka","Jan","Tluka",
                "mail@mail.com","1950-09-03", null);
        final UserJson user = userService.createUser(userJson);
        Assertions.assertThat(user).isNotNull();
        Assertions.assertThat(user.getUuid()).isNotBlank();
        Assertions.assertThat(user.getName()).isEqualTo("jtluka");
        Assertions.assertThat(user.getFirstName()).isEqualTo("Jan");
        Assertions.assertThat(user.getLastName()).isEqualTo("Tluka");
        Assertions.assertThat(user.getEmailAddress()).isEqualTo("mail@mail.com");
        Assertions.assertThat(user.getBirthDate()).isEqualTo("1950-09-03");
        Assertions.assertThat(user.getRegisteredOn()).isNotBlank();
    }

    @Test
    public void shouldUpdateUser() {
        final UserJson user = userService.getUser("josh@email.com");
        user.setFirstName("Petr");
        user.setLastName("Vanek");
        final boolean result = userService.updateUser(user);
        Assertions.assertThat(result).isTrue();
        final UserJson userModified = userService.getUser("josh@email.com");
        Assertions.assertThat(userModified).extracting(UserJson::getFirstName).isEqualTo("Petr");
        Assertions.assertThat(userModified).extracting(UserJson::getLastName).isEqualTo("Vanek");
    }

    @Test
    public void shouldDeleteUser() {
        final boolean result = userService.deleteUser("josh@email.com");
        Assertions.assertThat(result).isTrue();
        assertThrows(Exception.class, ()-> userService.getUser("josh@email.com"));
    }

    @Test
    public void shouldDeleteUserById() {
        final boolean result = userService.deleteUserById(UUID.fromString("deccc52a-4c7d-4f0c-9ba9-e12b6dd3c381"));
        Assertions.assertThat(result).isTrue();
        assertThrows(Exception.class, ()-> userService.getUserById(UUID.fromString("deccc52a-4c7d-4f0c-9ba9-e12b6dd3c381")));
    }

    @Test
    public void shouldThrowWhenNotFound() {
        assertThrows(NotFoundException.class,() -> userService.getUser("xxx@email.com"));
    }

    @Test
    public void shouldThrowWhenNotFoundById() {
        assertThrows(NotFoundException.class,() -> userService.getUserById(
                UUID.fromString("b1b44b12-34bc-4ed7-a666-9657b8b8c31b")));
    }

    @Test
    public void shouldGetUser() {
        UserJson userJson = userService.getUser("josh@email.com");
        Logger.getLogger(UserServiceTest.class.getName()).log(Level.INFO, userJson.toString());
        Assertions.assertThat(userJson).isNotNull();
        Assertions.assertThat(userJson).extracting(UserJson::getName).isEqualTo("jbloch");
        Assertions.assertThat(userJson).extracting(UserJson::getFirstName).isEqualTo("Josh");
        Assertions.assertThat(userJson).extracting(UserJson::getLastName).isEqualTo("Bloch");
        Assertions.assertThat(userJson).extracting(UserJson::getBirthDate).isEqualTo("1970-08-25");
        Assertions.assertThat(userJson).extracting(UserJson::getRegisteredOn).isEqualTo("2024-05-07");
    }

    @Test
    public void shouldGetUserById() {
        UserJson userJson = userService.getUserById(UUID.fromString("deccc52a-4c7d-4f0c-9ba9-e12b6dd3c381"));
        Logger.getLogger(UserServiceTest.class.getName()).log(Level.INFO, userJson.toString());
        Assertions.assertThat(userJson).isNotNull();
        Assertions.assertThat(userJson).extracting(UserJson::getName).isEqualTo("jbloch");
        Assertions.assertThat(userJson).extracting(UserJson::getFirstName).isEqualTo("Josh");
        Assertions.assertThat(userJson).extracting(UserJson::getLastName).isEqualTo("Bloch");
        Assertions.assertThat(userJson).extracting(UserJson::getEmailAddress).isEqualTo("josh@email.com");
        Assertions.assertThat(userJson).extracting(UserJson::getBirthDate).isEqualTo("1970-08-25");
        Assertions.assertThat(userJson).extracting(UserJson::getRegisteredOn).isEqualTo("2024-05-07");
    }

    @Test
    public void shouldGetAllUsers() {
        final List<UserJson> allUsers = userService.getAllUsers();
        Assertions.assertThat(allUsers).isNotNull();
        Assertions.assertThat(allUsers).isNotEmpty();
        for (final UserJson user : allUsers) {
            Assertions.assertThat(user.getName()).isNotEmpty();
            Assertions.assertThat(user.getFirstName()).isNotEmpty();
            Assertions.assertThat(user.getLastName()).isNotEmpty();
            Assertions.assertThat(user.getEmailAddress()).isNotEmpty();
            Assertions.assertThat(user.getBirthDate()).isNotEmpty();
            Assertions.assertThat(user.getRegisteredOn()).isNotEmpty();
        }
        var user = allUsers.stream().filter(x->x.getName().equals("jbloch")).findFirst().orElseGet(UserJson::new);
        Assertions.assertThat(user).extracting(UserJson::getEmailAddress).isEqualTo("josh@email.com");
        Assertions.assertThat(user).extracting(UserJson::getFirstName).isEqualTo("Josh");
        Assertions.assertThat(user).extracting(UserJson::getLastName).isEqualTo("Bloch");
        Assertions.assertThat(user).extracting(UserJson::getBirthDate).isEqualTo("1970-08-25");
        Assertions.assertThat(user).extracting(UserJson::getRegisteredOn).isEqualTo("2024-05-07");
    }

}