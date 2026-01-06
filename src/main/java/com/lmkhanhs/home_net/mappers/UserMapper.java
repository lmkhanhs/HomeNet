package com.lmkhanhs.home_net.mappers;

import org.mapstruct.Mapper;

import com.lmkhanhs.home_net.dtos.users.requests.CreateUserRequest;
import com.lmkhanhs.home_net.dtos.users.responses.UserResponse;
import com.lmkhanhs.home_net.entities.UserEntity;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserEntity toEntity(CreateUserRequest request);

    UserResponse toResponse(UserEntity entity);
}
