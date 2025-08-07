package com.example.usermanager.persistence;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;

import com.example.usermanager.model.User;
import com.example.usermanager.persistence.exception.NotFoundException;

import static org.junit.jupiter.api.Assertions.assertThrows;


@JdbcTest
//overrides default h2
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
public class UsersDaoTest {

    @Autowired
    UsersDao usersDao;

    @Test
    public void shouldGetUser() {
        final User user = usersDao.getOne(1);
        System.out.println(user);
        Assertions.assertThat(user).isNotNull();
        Assertions.assertThat(user).extracting(User::getId).isEqualTo(1L);
        Assertions.assertThat(user).extracting(User::getFirstName).isEqualTo("Josh");
    }

    @Test
    public void shouldThrowNotFound() {
        assertThrows(NotFoundException.class, () -> usersDao.getOne(1234));
    }

    @Test
    public void shouldCreateUser() {
        User user = new User();
        user.setFirstName("Pavel");
        user.setLastName("Urbanek");
        final User createdUser = usersDao.createOne(user);
        Assertions.assertThat(createdUser).isNotNull();
        Assertions.assertThat(createdUser.getId()).isGreaterThan(0);
        final User userFound = usersDao.getOne(user.getId());
        Assertions.assertThat(userFound).isEqualTo(user);
    }

    @Test
    public void shouldUpdateUser() {
        final User user = usersDao.getOne(1);
        user.setFirstName("Michal");
        user.setLastName("Trava");
        final int result = usersDao.updateOne(user);
        Assertions.assertThat(result).isEqualTo(1);
        final User userFound = usersDao.getOne(1);
        Assertions.assertThat(userFound).isEqualTo(user);
    }

    @Test
    public void shouldDeleteUser() {
        final User user = usersDao.getOne(1);
        Assertions.assertThat(user).isNotNull();
        final int result = usersDao.deleteOne(user);
        Assertions.assertThat(result).isEqualTo(1);
        assertThrows(Exception.class, () -> usersDao.getOne(1));
    }

    @Test
    public void shouldGetAllUsers() {
        final List<User> allUsers = usersDao.getAll();
        Assertions.assertThat(allUsers).isNotNull();
        for (final User user : allUsers) {
            Assertions.assertThat(user).isNotNull();
            Assertions.assertThat(user).extracting(User::getFirstName).isNotNull();
            Assertions.assertThat(user).extracting(User::getLastName).isNotNull();
        }
    }

}
