package com.example.usermanager.persistence;

import java.text.ParseException;
import java.util.List;
import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;

import com.example.usermanager.model.User;
import com.example.usermanager.persistence.exception.ExistingUserConflict;
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
    public void shouldGetUserByEmail() {
        final User user = usersDao.getOneByEmail("josh@email.com");
        System.out.println(user);
        Assertions.assertThat(user).isNotNull();
        Assertions.assertThat(user).extracting(User::getFirstName).isEqualTo("Josh");
    }

    @Test
    public void shouldGetUserByUuid() {
        final User user = usersDao.getOneByUuid(UUID.fromString("deccc52a-4c7d-4f0c-9ba9-e12b6dd3c381"));
        System.out.println(user);
        Assertions.assertThat(user).isNotNull();
        Assertions.assertThat(user).extracting(User::getFirstName).isEqualTo("Josh");
    }

    @Test
    public void shouldFailGetUserByUuid() {
        assertThrows(NotFoundException.class, () -> usersDao.getOneByUuid(
                UUID.fromString("b1b44b12-34bc-4ed7-a666-9657b8b8c31b")));
    }

    @Test
    public void shouldThrowNotFound() {
        assertThrows(NotFoundException.class, () -> usersDao.getOne(1234));
    }

    @Test
    public void shouldCreateUser() throws ParseException {
        User user = new User();
        user.setName("purban");
        user.setFirstName("Pavel");
        user.setLastName("Urbanek");
        user.setEmail("pavel@seznam.cz");
        user.setBirthDate(User.dateFormatter.parse("2007-09-07"));
        final User createdUser = usersDao.createOne(user);
        Assertions.assertThat(createdUser).isNotNull();
        Assertions.assertThat(createdUser.getId()).isNotNull();
        Assertions.assertThat(createdUser.getId()).isGreaterThan(0);
        Assertions.assertThat(createdUser.getUuid()).isNotNull();
        Assertions.assertThat(createdUser.getName()).isEqualTo("purban");
        Assertions.assertThat(createdUser.getFirstName()).isEqualTo("Pavel");
        Assertions.assertThat(createdUser.getLastName()).isEqualTo("Urbanek");
        Assertions.assertThat(createdUser.getEmail()).isNotNull();
        Assertions.assertThat(createdUser.getEmail()).isEqualTo("pavel@seznam.cz");
        Assertions.assertThat(createdUser).extracting(User::getBirthDate).isNotNull();
        Assertions.assertThat(createdUser).extracting(User::getBirthDate).isEqualTo(User.dateFormatter.parse("2007-09-07"));
        Assertions.assertThat(createdUser).extracting(User::getRegisteredDate).isNotNull();
        final User userFound = usersDao.getOne(user.getId());
        Assertions.assertThat(userFound).isEqualTo(user);
    }

    @Test
    public void shouldFailCreateUser() throws ParseException {
        User user = new User();
        user.setName("jbloch"); //duplicated name
        user.setFirstName("Jindra");
        user.setLastName("Bloch");
        user.setEmail("jb@seznam.cz");
        user.setBirthDate(User.dateFormatter.parse("2007-09-07"));
        assertThrows(ExistingUserConflict.class, () -> usersDao.createOne(user));
    }

    @Test
    public void shouldFailCreateUser2() throws ParseException {
        User user = new User();
        user.setName("pbloch");
        user.setFirstName("Petr");
        user.setLastName("Bloch");
        user.setEmail("josh@email.com"); // duplicated email
        user.setBirthDate(User.dateFormatter.parse("2007-09-07"));
        assertThrows(ExistingUserConflict.class, () -> usersDao.createOne(user));
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
    public void shouldFailUpdateUser() {
        final User user = usersDao.getOne(1);
        user.setFirstName("Michal");
        user.setLastName("Trava");
        user.setEmail("jrotten@email.com");
        assertThrows(ExistingUserConflict.class, () -> usersDao.updateOne(user));
    }

    @Test
    public void shouldDeleteByEmail() {
        final User user = usersDao.getOne(1);
        Assertions.assertThat(user).isNotNull();
        final int result = usersDao.deleteOne(user.getEmail());
        Assertions.assertThat(result).isEqualTo(1);
        assertThrows(Exception.class, () -> usersDao.getOne(1));
    }

    @Test
    public void shouldCountUsers() {
        final Long count = usersDao.count();
        Assertions.assertThat(count).isNotNull();
        Assertions.assertThat(count).isGreaterThan(0);
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
    public void shouldDeleteUserById() {
        final User user = usersDao.getOne(1);
        Assertions.assertThat(user).isNotNull();
        final int result = usersDao.deleteOneById(user.getUuid());
        Assertions.assertThat(result).isEqualTo(1);
        assertThrows(Exception.class, () -> usersDao.getOne(1));
    }

    @Test
    public void shouldFailDeleteUserById() {
        final int result = usersDao.deleteOneById(UUID.fromString("b1b44b12-34bc-4ed7-a666-9657b8b8c31b"));
        Assertions.assertThat(result).isEqualTo(0);
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
