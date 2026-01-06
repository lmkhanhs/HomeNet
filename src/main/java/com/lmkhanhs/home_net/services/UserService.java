package com.lmkhanhs.home_net.services;

import java.util.List;

import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.stereotype.Service;

import com.lmkhanhs.home_net.dtos.users.requests.CreateUserRequest;
import com.lmkhanhs.home_net.dtos.users.responses.UserResponse;
import com.lmkhanhs.home_net.entities.UserEntity;
import com.lmkhanhs.home_net.exceptions.AppException;
import com.lmkhanhs.home_net.exceptions.ErrorCode;
import com.lmkhanhs.home_net.mappers.UserMapper;
import com.lmkhanhs.home_net.repositories.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;

    // Handle create user
    public UserResponse handleCreateUser(CreateUserRequest request) {
        if (this.userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new AppException(ErrorCode.USERNAME_EXISTED, "Please use another username!");
        }
        UserEntity user = this.userMapper.toEntity(request);
        user.setAvatarUrl("http://localhost:8088/images/a279be82-eeba-4091-b05f-85bdf3c236f3.jpg");
        user.setIsActive(true);
        this.userRepository.save(user);
        return this.userMapper.toResponse(user);
    }
    // Get all users
    public List<UserResponse> handleGetAllUsers() {
        List<UserEntity> users = this.userRepository.findAllByIsActiveTrue();
        return users.stream()
                .map(user -> this.userMapper.toResponse(user))
                .toList();
    }
}
