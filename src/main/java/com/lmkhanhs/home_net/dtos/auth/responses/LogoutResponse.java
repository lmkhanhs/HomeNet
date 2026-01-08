package com.lmkhanhs.home_net.dtos.auth.responses;
import lombok.Builder;

import lombok.AllArgsConstructor;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LogoutResponse {
    boolean success;
}