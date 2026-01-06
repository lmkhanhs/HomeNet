package com.lmkhanhs.home_net.dtos.users.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class UserResponse {
    String id;
    String username;
    String password;
    String email;
    String fullName;
    String phoneNumber;
    String address;
    String city;
    String ward;

    Boolean isActive;
    String avatarUrl;
}
