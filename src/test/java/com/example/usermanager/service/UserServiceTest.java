package com.example.usermanager.service;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;

import com.example.usermanager.model.rest.UserJson;

@JdbcTest
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
class UserServiceTest {

    @Autowired
    UserService userService;

    @Test
    public void shouldCreateUser() {
        UserJson userJson = new UserJson("jtluka","Jan","Tluka",
                "mail@mail.com","1950-09-03", null);
        final boolean result = userService.createUser(userJson);
        Assertions.assertThat(result).isTrue();
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

}