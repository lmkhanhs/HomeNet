package com.lmkhanhs.home_net.dtos.tenants.responses;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.*;
import lombok.experimental.FieldDefaults;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TenantResponse {
    String _id;
    String status;
    String name;
    LocalDate fromDate;
    LocalDate toDate;
}
