package com.example.usermanager.model;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;
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

    Map<String,String> remapping = Map.of(
            "emailAddress","email",
            "registeredOn","registeredDate");

    default Map<String,String> mapParameters(UserJson search) {
        Map<String,String> sourceMap = Arrays.stream(UserJson.class.getDeclaredFields())
                .map((Function<Field, Optional<Pair<String, String>>>) x -> {
                    try {
                        x.setAccessible(true);
                        if (x.get(search) != null) {
                            return Optional.of(Pair.of(x.getName(), x.get(search).toString()));
                        } else {
                            return Optional.empty();
                        }
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                })
                .filter(Optional::isPresent).map(Optional::get)
                .collect(Collectors.toMap(Pair::getKey,Pair::getValue));

        Map<String,String> targetColumns = Arrays.stream(User.class.getDeclaredFields())
                .filter(x->x.isAnnotationPresent(Column.class))
                .collect(Collectors.toMap(Field::getName, x->x.getAnnotation(Column.class).value()));

        return sourceMap.entrySet().stream()
                .map((Function<Map.Entry<String,String>, Optional<Pair<String, String>>>) x -> {
                    String key = targetColumns.get(remapping.getOrDefault(x.getKey(), x.getKey()));
                    if (key != null){
                        return Optional.of(Pair.of(key, x.getValue()));
                    } else {
                        return Optional.empty();
                    }
                }).filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toMap(Pair::getKey,Pair::getValue));
    }

}
