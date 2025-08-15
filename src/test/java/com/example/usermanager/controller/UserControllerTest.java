package com.example.usermanager.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static com.example.usermanager.controller.UserController.BASE_URL;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.example.usermanager.model.rest.UserJson;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    UserJson testUser = UserJson.builder()
            .uuid("deccc52a-4c7d-4f0c-9ba9-e12b6dd3c381")
            .name("jbloch")
            .firstName("Josh")
            .lastName("Bloch")
            .emailAddress("josh@email.com")
            .birthDate("1970-08-25")
            .registeredOn("2024-05-07")
            .build();

    @Test
    public void shouldReturnAllUsers() throws Exception {
        this.mockMvc.perform(get(BASE_URL + "/")).andDo(print()).andExpect(status().isOk())
                .andDo(mvcResult -> {
                String json = mvcResult.getResponse().getContentAsString();
                    final List<UserJson> users = convertJSONStringToList(json, UserJson.class);
                    Assertions.assertThat(users).isNotNull();
                    Assertions.assertThat(users).isNotEmpty();
                    Assertions.assertThat(users.size()).isGreaterThan(1);

                    Optional<UserJson> user = users.stream().filter(x -> x.getName().equals("jbloch")).findFirst();
                    Assertions.assertThat(user).isPresent();
                    Assertions.assertThat(user.get()).isEqualTo(testUser);
            });

    }

    @Test
    public void shouldGetOneUser() throws Exception {
        this.mockMvc.perform(get(BASE_URL + "/user").param("uuid",
                        "deccc52a-4c7d-4f0c-9ba9-e12b6dd3c381")).andDo(print()).andExpect(status().isOk())
                .andDo(mvcResult -> {
                    String json = mvcResult.getResponse().getContentAsString();
                    UserJson user = convertJSONStringToObject(json, UserJson.class);
                    Assertions.assertThat(user.getName()).isEqualTo("jbloch");
                    Assertions.assertThat(user.getFirstName()).isEqualTo("Josh");
                    Assertions.assertThat(user.getLastName()).isEqualTo("Bloch");
                    Assertions.assertThat(user.getEmailAddress()).isEqualTo("josh@email.com");
                    Assertions.assertThat(user.getBirthDate()).isEqualTo("1970-08-25");
                    Assertions.assertThat(user.getRegisteredOn()).isEqualTo("2024-05-07");
                });

    }

    @Test
    public void shouldFailGetOneUser() throws Exception {
        this.mockMvc.perform(get(BASE_URL + "/user").param("uuid",
                        "deccc52a-4c7d-4f0c")).andDo(print()).andExpect(status().isBadRequest())
                .andDo(mvcResult -> {
                    String json = mvcResult.getResponse().getContentAsString();
                    System.out.println(json);
                });

    }

    @Test
    public void shouldFailGetOneUser2() throws Exception {
        this.mockMvc.perform(get(BASE_URL + "/user").param("uuid",
                        "b1b44b12-34bc-4ed7-a666-9657b8b8c31b")).andDo(print()).andExpect(status().isNotFound())
                .andDo(mvcResult -> {
                    String json = mvcResult.getResponse().getContentAsString();
                    System.out.println(json);
                });

    }

    @Test
    @Transactional
    public void shouldCreateUser() throws Exception {
        this.mockMvc.perform(post(BASE_URL + "/user").content("""
                        {
                          "name": "mtrava",
                          "firstName": "Michal",
                          "lastName": "Trava",
                          "emailAddress": "trava@seznam.cz",
                          "birthDate": "1972-08-25",
                          "registeredOn": "2025-05-07"
                        }
                        """).contentType(MediaType.APPLICATION_JSON_VALUE)).andDo(print()).andExpect(status().isCreated())
                .andDo(mvcResult -> {
                    String json = mvcResult.getResponse().getContentAsString();
                    System.out.println(json);
                });

    }

    @Test
    @Transactional
    public void shouldFailCreateUser() throws Exception { //already exists
        this.mockMvc.perform(post(BASE_URL + "/user").content("""
                        {
                          "name": "jbloch",
                          "firstName": "Josh",
                          "lastName": "Twin",
                          "emailAddress": "twin@mail.com",
                          "birthDate": "1970-08-25",
                          "registeredOn": "2022-05-07"
                        }
                        """).contentType(MediaType.APPLICATION_JSON_VALUE)).andDo(print()).andExpect(status().isConflict())
                .andDo(mvcResult -> {
                    String json = mvcResult.getResponse().getContentAsString();
                    System.out.println(json);
                });

    }

    @Test
    @Transactional
    public void shouldFailCreateUser2() throws Exception { //name too long
        this.mockMvc.perform(post(BASE_URL + "/user").content("""
                        {
                          "name": "string_too_long",
                          "firstName": "Pepa",
                          "lastName": "ZDepa",
                          "emailAddress": "pepa@mail.com",
                          "birthDate": "1970-08-25",
                          "registeredOn": "2022-05-07"
                        }
                        """).contentType(MediaType.APPLICATION_JSON_VALUE)).andDo(print()).andExpect(status().isBadRequest())
                .andDo(mvcResult -> {
                    String json = mvcResult.getResponse().getContentAsString();
                    System.out.println(json);
                });

    }

    @Test
    @Transactional
    public void shouldFailCreateUser3() throws Exception { //bad date format
        this.mockMvc.perform(post(BASE_URL + "/user").content("""
                        {
                          "name": "pzdepa",
                          "firstName": "Pepa",
                          "lastName": "ZDepa",
                          "emailAddress": "pepa@mail.com",
                          "birthDate": "08-25-1970",
                          "registeredOn": "05-07-2022"
                        }
                        """).contentType(MediaType.APPLICATION_JSON_VALUE)).andDo(print()).andExpect(status().isBadRequest())
                .andDo(mvcResult -> {
                    String json = mvcResult.getResponse().getContentAsString();
                    System.out.println(json);
                });

    }

    @Test
    @Transactional
    public void shouldUpdateUser() throws Exception {
        this.mockMvc.perform(put(BASE_URL + "/user").content("""
                        {
                          "uuid" : "deccc52a-4c7d-4f0c-9ba9-e12b6dd3c381",
                          "name": "pzdepa",
                          "firstName": "Pepa",
                          "lastName": "ZDepa",
                          "emailAddress": "pepa@mail.com",
                          "birthDate": "1970-08-25",
                          "registeredOn": "2022-05-07"
                        }
                        """).contentType(MediaType.APPLICATION_JSON_VALUE)).andDo(print()).andExpect(status().isNoContent())
                .andDo(mvcResult -> {
                    String json = mvcResult.getResponse().getContentAsString();
                    System.out.println(json);
                });

    }

    @Test
    @Transactional
    public void shouldFailUpdateUser() throws Exception { //conflicting email
        this.mockMvc.perform(put(BASE_URL + "/user").content("""
                        {
                          "uuid" : "deccc52a-4c7d-4f0c-9ba9-e12b6dd3c381",
                          "name": "pzdepa",
                          "firstName": "Pepa",
                          "lastName": "ZDepa",
                          "emailAddress": "jrotten@email.com",
                          "birthDate": "1970-08-25",
                          "registeredOn": "2022-05-07"
                        }
                        """).contentType(MediaType.APPLICATION_JSON_VALUE)).andDo(print()).andExpect(status().isConflict())
                .andDo(mvcResult -> {
                    String json = mvcResult.getResponse().getContentAsString();
                    System.out.println(json);
                });

    }

    @Test
    @Transactional
    public void shouldFailUpdateUser2() throws Exception {
        this.mockMvc.perform(put(BASE_URL + "/user").content("""
                        {
                          "uuid" : "deccc52a-4c7d-4f0c-9ba9-e12b6dd3c381",
                          "name": "pzdepaaaaaaaaaaaaaaaaaaaaaaa",
                          "firstName": "Pepa",
                          "lastName": "ZDepa",
                          "emailAddress": "foo@email.com",
                          "birthDate": "1970-08-25",
                          "registeredOn": "2022-05-07"
                        }
                        """).contentType(MediaType.APPLICATION_JSON_VALUE)).andDo(print()).andExpect(status().isBadRequest())
                .andDo(mvcResult -> {
                    String json = mvcResult.getResponse().getContentAsString();
                    System.out.println(json);
                });

    }

    @Test
    @Transactional
    public void shouldFailUpdateUser3() throws Exception { //nonexistent uuid
        this.mockMvc.perform(put(BASE_URL + "/user").content("""
                        {
                          "uuid" : "8ffe0a11-b6cc-4dbe-80af-ad0775010de1",
                          "name": "pzdepa",
                          "firstName": "Pepa",
                          "lastName": "ZDepa",
                          "emailAddress": "jrotten@email.com",
                          "birthDate": "1970-08-25",
                          "registeredOn": "2022-05-07"
                        }
                        """).contentType(MediaType.APPLICATION_JSON_VALUE)).andDo(print()).andExpect(status().isBadRequest())
                .andDo(mvcResult -> {
                    String json = mvcResult.getResponse().getContentAsString();
                    System.out.println(json);
                });

    }

    @Test
    @Transactional
    public void shouldDeleteUser() throws Exception {
        this.mockMvc.perform(delete(BASE_URL + "/user").param("uuid", "deccc52a-4c7d-4f0c-9ba9-e12b6dd3c381")
                .contentType(MediaType.APPLICATION_JSON_VALUE)).andDo(print()).andExpect(status().isNoContent())
                .andDo(mvcResult -> {
                    String json = mvcResult.getResponse().getContentAsString();
                    System.out.println(json);
                });

    }

    @Test
    @Transactional
    public void shouldFailDeleteUser() throws Exception { //nonexistent UUID
        this.mockMvc.perform(delete(BASE_URL + "/user").param("uuid", "8ffe0a11-b6cc-4dbe-80af-ad0775010de1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)).andDo(print()).andExpect(status().isNotFound())
                .andDo(mvcResult -> {
                    String json = mvcResult.getResponse().getContentAsString();
                    System.out.println(json);
                });

    }

    @Test
    @Transactional
    public void shouldFailDeleteUser2() throws Exception { //bad UUID
        this.mockMvc.perform(delete(BASE_URL + "/user").param("uuid", "deccc52a-4c7d-4f0c-9ba9")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)).andDo(print()).andExpect(status().isBadRequest())
                .andDo(mvcResult -> {
                    String json = mvcResult.getResponse().getContentAsString();
                    System.out.println(json);
                });

    }

    public static <T>  T convertJSONStringToObject(String json, Class<T> objectClass) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        return mapper.readValue(json, objectClass);
    }

    public static <T> List<T> convertJSONStringToList(String json, Class<T> objectClass) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        final CollectionType collectionType = TypeFactory.createDefaultInstance().constructCollectionType(List.class, objectClass);
        return mapper.readValue(json, collectionType);
    }

}
