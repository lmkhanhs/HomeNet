package com.lmkhanhs.home_net.dtos.amenities.requests;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Getter
@Setter
@NoArgsConstructor
public class CreateAmenityRequest {
    String name;
    String description;
    MultipartFile icon;
}
