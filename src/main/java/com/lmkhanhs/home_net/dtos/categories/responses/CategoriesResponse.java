package com.lmkhanhs.home_net.dtos.categories.responses;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class CategoriesResponse {
    Long id;
    String iconUrl;
    String description;
    Integer position;
    String name;
}
