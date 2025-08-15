package com.example.usermanager.model;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.example.usermanager.model.rest.UserJson;

@Mapper
public interface EntityMapper {
    EntityMapper INSTANCE = Mappers.getMapper(EntityMapper.class);

    @Mapping(target = "email", source = "emailAddress")
    @Mapping(target = "birthDate", source = "birthDate", dateFormat = User.DATE_FORMAT)
    @Mapping(target = "registeredDate", source = "registeredOn", dateFormat = User.DATE_FORMAT)
    User userJsonToUser(UserJson entity);

    @Mapping(target = "emailAddress", source = "email")
    @Mapping(target = "birthDate", source = "birthDate", dateFormat = UserJson.DATE_FORMAT)
    @Mapping(target = "registeredOn", source = "registeredDate", dateFormat = UserJson.DATE_FORMAT)
    UserJson userToUserJson(User dto);

    List<UserJson> mapToJson(List<User> users);
    List<User> mapToEntity(List<UserJson> employees);
}
