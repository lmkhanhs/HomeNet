package com.lmkhanhs.home_net.dtos.tenants.requests;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateTenantRequest {
    String name;
    LocalDate fromDate;
    LocalDate toDate;
}
