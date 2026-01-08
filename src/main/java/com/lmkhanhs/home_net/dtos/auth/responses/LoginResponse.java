package com.lmkhanhs.home_net.dtos.auth.responses;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    String tokenType;
    String accessToken;
    Integer expiresIn;
    String refreshToken;
}