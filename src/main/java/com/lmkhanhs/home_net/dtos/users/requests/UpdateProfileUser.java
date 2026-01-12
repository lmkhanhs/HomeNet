package com.lmkhanhs.home_net.dtos.users.requests;

import org.springframework.web.multipart.MultipartFile;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateProfileUser {
    String email;
    String fullName;
    String phoneNumber;
    String address;
    String city;
    String district;
    String countryId;
    MultipartFile avatar;
}
