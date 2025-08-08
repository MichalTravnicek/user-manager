package com.example.usermanager.model;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.example.usermanager.model.rest.UserJson;

@Mapper
public interface EntityMapper {
    EntityMapper INSTANCE = Mappers.getMapper(EntityMapper.class);

    @Mapping(target = "email", source = "emailAddress")
    @Mapping(target = "birthDate", source = "birthDate", dateFormat = "yyyy-MM-dd")
    @Mapping(target = "registeredDate", source = "registeredOn", dateFormat = "yyyy-MM-dd")
    User userJsonToUser(UserJson entity);

    @Mapping(target = "emailAddress", source = "email")
    @Mapping(target = "birthDate", source = "birthDate", dateFormat = "yyyy-MM-dd")
    @Mapping(target = "registeredOn", source = "registeredDate", dateFormat = "yyyy-MM-dd")
    UserJson userToUserJson(User dto);
}
