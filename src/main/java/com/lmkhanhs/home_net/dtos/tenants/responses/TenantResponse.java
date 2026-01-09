package com.lmkhanhs.home_net.dtos.tenants.responses;

import java.time.LocalDate;

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
public class TenantResponse {
    String _id;
    String status;
    String name;
    LocalDate fromDate;
    LocalDate toDate;
}
