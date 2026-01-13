package com.lmkhanhs.home_net.controllers;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.lmkhanhs.home_net.dtos.amenities.requests.CreateAmenityRequest;
import com.lmkhanhs.home_net.dtos.amenities.requests.UpdateAmenityRequest;
import com.lmkhanhs.home_net.dtos.amenities.responses.AmenityResponse;
import com.lmkhanhs.home_net.dtos.apps.ApiResponse;
import com.lmkhanhs.home_net.services.AmenityService;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;


@RestController
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("${app.prefix}/amenities")
public class AmenityController {
    AmenityService amenityService;

    // create amenity 
    @PreAuthorize("hasAuthority('CREATE_AMENITY')")
    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(org.springframework.http.HttpStatus.CREATED)
    public ApiResponse<AmenityResponse> createAmenity(@ModelAttribute CreateAmenityRequest createAmenityRequest) {
        return ApiResponse.<AmenityResponse>builder()
                .code(201)
                .message("Create amenity successfully!")
                .data(this.amenityService.handleCreateAmenity(createAmenityRequest))
                .build();
    }
    // update amenity 
    @PreAuthorize("hasAuthority('UPDATE_AMENITY')")
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<AmenityResponse> updateAmenity(@org.springframework.web.bind.annotation.PathVariable Long id,
            @ModelAttribute UpdateAmenityRequest updateAmenityRequest) {
        return ApiResponse.<AmenityResponse>builder()
                .code(200)
                .message("Update amenity successfully!")
                .data(this.amenityService.handleUpdateAmenity(id, updateAmenityRequest))
                .build();
    }
    // delete amenity
    @PreAuthorize("hasAuthority('DELETE_AMENITY')")
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(org.springframework.http.HttpStatus.NO_CONTENT)
    public ApiResponse<Void> deleteAmenity(@org.springframework.web.bind.annotation.PathVariable Long id) {
        this.amenityService.handleDeleteAmenity(id);
        return ApiResponse.<Void>builder()
                .code(204)
                .message("Delete amenity successfully!")
                .build();
    }
    // restore amenity
    @PreAuthorize("hasAuthority('RESTORE_AMENITY')")
    @PutMapping(value = "/{id}/restore")
    @ResponseStatus(org.springframework.http.HttpStatus.NO_CONTENT)
    public ApiResponse<Void> restoreAmenity(@org.springframework.web.bind.annotation.PathVariable Long id) {
        this.amenityService.handleRestoreAmenity(id);
        return ApiResponse.<Void>builder()
                .code(204)
                .message("Restore amenity successfully!")
                .build();
    }
    // get all amenities by pageable
    @GetMapping(value = "")
    public ApiResponse<List<AmenityResponse>> getAmenitiesByPage(Pageable pageable) {
        var amenities = this.amenityService.handleGetAmenitiesByPageable(pageable);
        return ApiResponse.<List<AmenityResponse>>builder()
                .code(200)
                .message("Get amenities successfully!")
                .data(amenities)
                .build();
    }
    // get amenity by id
    @GetMapping(value = "/{amenityId}")
    public ApiResponse<AmenityResponse> getAmenityById(@org.springframework.web.bind.annotation.PathVariable Long amenityId) {
        return ApiResponse.<AmenityResponse>builder()
                .code(200)
                .message("Get amenity successfully!")
                .data(this.amenityService.handleGetAmenityById(amenityId))
                .build();
    }

}