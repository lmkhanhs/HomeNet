package com.lmkhanhs.home_net.services;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.lmkhanhs.home_net.context.TenantContext;
import com.lmkhanhs.home_net.dtos.users.requests.CreateUserRequest;
import com.lmkhanhs.home_net.dtos.users.responses.UserResponse;
import com.lmkhanhs.home_net.entities.RoleEntity;
import com.lmkhanhs.home_net.entities.UserEntity;
import com.lmkhanhs.home_net.exceptions.AppException;
import com.lmkhanhs.home_net.exceptions.ErrorCode;
import com.lmkhanhs.home_net.mappers.UserMapper;
import com.lmkhanhs.home_net.repositories.RoleRepository;
import com.lmkhanhs.home_net.repositories.UserRepository;
import com.lmkhanhs.home_net.utils.AuthenticationUtills;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    AuthenticationUtills authenticationUtills;
    UserRepository userRepository;
    UserMapper userMapper;
    RoleRepository roleRepository;
    PasswordEncoder passwordEncoder;
    // Handle create user
    public UserResponse handleCreateUser(String tenantId, CreateUserRequest request) {
        if (this.userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new AppException(ErrorCode.USERNAME_EXISTED, "Please use another username!");
        }
        RoleEntity roleEntity = roleRepository.findByNameAndTenantId("USER", tenantId)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND, "Default role USER not found!"));

        UserEntity user = this.userMapper.toEntity(request);
        user.setAvatarUrl("http://localhost:8088/images/a279be82-eeba-4091-b05f-85bdf3c236f3.jpg");
        user.setIsActive(true);
        user.setTenantId(tenantId);
        user.setPassword(this.passwordEncoder.encode(request.getPassword()));
        user.getRoles().add(roleEntity);
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
    public UserResponse handleGetMyInfo(String tenantId) {   
        String username = this.authenticationUtills.getUserName();
        UserEntity user = this.userRepository.findByUsernameAndTenantId(username, tenantId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND, "User not found!"));
        return this.userMapper.toResponse(user);
    }
}
