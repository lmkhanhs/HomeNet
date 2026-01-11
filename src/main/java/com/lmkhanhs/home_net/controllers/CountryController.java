package com.lmkhanhs.home_net.controllers;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.lmkhanhs.home_net.dtos.apps.ApiResponse;
import com.lmkhanhs.home_net.dtos.countries.requests.CreateCountryRequest;
import com.lmkhanhs.home_net.dtos.countries.responses.CountryResponse;
import com.lmkhanhs.home_net.services.CountryService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;



@RestController
@RequestMapping("${app.prefix}/countries")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CountryController {
    CountryService countryService; 

    @PreAuthorize("hasAuthority('CREATE_COUNTRY')")
    @PostMapping( value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(org.springframework.http.HttpStatus.CREATED)
    public ApiResponse<CountryResponse> createCountry(@ModelAttribute CreateCountryRequest request) {
        
        return ApiResponse.<CountryResponse>builder()
            .code(201)
            .message("Create country successfully!")
            .data(
                this.countryService.handleCreateCountry(request)
            )
            .build();
    }
    @GetMapping("")
    public ApiResponse<List<CountryResponse>> getCountries(Pageable pageable) {
        return ApiResponse.<List<CountryResponse>>builder()
            .code(200)
            .message("Get countries successfully!")
            .data(
                this.countryService.handleGetAllCountries(pageable)
            )
            .build();
    }
    
}
