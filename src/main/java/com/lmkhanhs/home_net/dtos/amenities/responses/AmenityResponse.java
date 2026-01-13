package com.lmkhanhs.home_net.dtos.amenities.responses;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Getter
@Setter
@NoArgsConstructor
public class AmenityResponse {
    Long id;
    String name;
    String description;
    String iconUrl;
    Integer position;
}
