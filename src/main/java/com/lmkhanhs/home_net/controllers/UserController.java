package com.lmkhanhs.home_net.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lmkhanhs.home_net.dtos.apps.ApiResponse;
import com.lmkhanhs.home_net.dtos.users.requests.CreateUserRequest;
import com.lmkhanhs.home_net.dtos.users.responses.UserResponse;
import com.lmkhanhs.home_net.services.UserService;
import com.lmkhanhs.home_net.utils.RequestHttpUitlls;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;



@RestController
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("${app.prefix}/users")
@Slf4j
public class UserController {
    UserService userService;
    RequestHttpUitlls requestHttpUitlls;

    @PostMapping(value = "")
    public ApiResponse<UserResponse> createUser( HttpServletRequest req, @RequestBody CreateUserRequest request) {
        log.info(this.requestHttpUitlls.getTenantIDValid(req));
        return ApiResponse.<UserResponse>builder()
                .data(this.userService.handleCreateUser(this.requestHttpUitlls.getTenantIDValid(req), request))
                .build();
    }
    @GetMapping("")
    public ApiResponse<List<UserResponse>> getAllUsers() {
        return ApiResponse.<List<UserResponse>>builder()
                .data(this.userService.handleGetAllUsers())
                .build();
    }
    
}
