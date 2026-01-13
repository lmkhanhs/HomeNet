package com.lmkhanhs.home_net.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.lmkhanhs.home_net.context.TenantContext;
import com.lmkhanhs.home_net.dtos.amenities.requests.CreateAmenityRequest;
import com.lmkhanhs.home_net.dtos.amenities.requests.UpdateAmenityRequest;
import com.lmkhanhs.home_net.dtos.amenities.responses.AmenityResponse;
import com.lmkhanhs.home_net.entities.AmenityEntity;
import com.lmkhanhs.home_net.exceptions.AppException;
import com.lmkhanhs.home_net.exceptions.ErrorCode;
import com.lmkhanhs.home_net.mappers.AmenityMapper;
import com.lmkhanhs.home_net.repositories.AmenityRepository;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class AmenityService {
    AmenityRepository amenityRepository;
    AmenityMapper amenityMapper;
    // create
    public AmenityResponse handleCreateAmenity(CreateAmenityRequest createAmenityRequest) {
        String tenantId =  TenantContext.getTenant();
        // check exist
        if (amenityRepository.existsByNameAndTenantId(createAmenityRequest.getName(), tenantId)) {
            throw new AppException(ErrorCode.AMENITY_EXISTED, "Try again with different amenity name");
        }
        
        var amenity = amenityMapper.toAmenityEntity(createAmenityRequest);
        amenity.setTenantId(tenantId);
        amenity.setPosition( (int)this.amenityRepository.count());
        return this.amenityMapper.toAmenityResponse(amenityRepository.save(amenity));
    }
    // handle update
    public AmenityResponse handleUpdateAmenity(Long id, UpdateAmenityRequest updateAmenityRequest) {
        String tenantId =  TenantContext.getTenant();
        AmenityEntity amenity = amenityRepository.findByIdAndTenantIdAndIsDeletedFalse(id, tenantId)
            .orElseThrow(() -> new AppException(ErrorCode.AMENITY_NOT_FOUND, " Try again with another amenity id " ));
        // check name exist
        if (amenityRepository.existsByNameAndTenantId(updateAmenityRequest.getName(), tenantId)) {
            AmenityEntity existingAmenity = amenityRepository.findByNameAndTenantId(updateAmenityRequest.getName(), tenantId).get();
            if (!existingAmenity.getId().equals(id)) {
                throw new AppException(ErrorCode.AMENITY_EXISTED, "Try again with different amenity name");
            }
        }
        this.amenityMapper.updateAmenityEntity(updateAmenityRequest, amenity);
        return this.amenityMapper.toAmenityResponse(amenityRepository.save(amenity));
    }
    // delete amenity
    public void handleDeleteAmenity(Long id) {
        String tenantId =  TenantContext.getTenant();
        AmenityEntity amenity = amenityRepository.findByIdAndTenantIdAndIsDeletedFalse(id, tenantId)
            .orElseThrow(() -> new AppException(ErrorCode.AMENITY_NOT_FOUND, " Try again with another amenity id " ));
        
        amenity.setIsDeleted(true);
        amenityRepository.save(amenity);
    }
    // restore amenity
    public void handleRestoreAmenity(Long id) {
        String tenantId =  TenantContext.getTenant();
        AmenityEntity amenity = amenityRepository.findByIdAndTenantIdAndIsDeletedTrue(id, tenantId)
            .orElseThrow(() -> new AppException(ErrorCode.AMENITY_NOT_FOUND, " Try again with another amenity id " ));
        
        amenity.setIsDeleted(false);
        amenityRepository.save(amenity);
    }
    // get amenity by id
    public AmenityResponse handleGetAmenityById(Long id) {
        String tenantId =  TenantContext.getTenant();
        AmenityEntity amenity = amenityRepository.findByIdAndTenantIdAndIsDeletedFalse(id, tenantId)
            .orElseThrow(() -> new AppException(ErrorCode.AMENITY_NOT_FOUND, " Try again with another amenity id " ));
        
        return this.amenityMapper.toAmenityResponse(amenity);
    }
    // get by pageable
    public List<AmenityResponse> handleGetAmenitiesByPageable(org.springframework.data.domain.Pageable pageable) {
        String tenantId =  TenantContext.getTenant();
        List<AmenityEntity> amenities = amenityRepository.findAllByTenantIdAndIsDeletedFalse(tenantId, pageable).getContent();
        return amenities.stream()
                .map(amenity -> this.amenityMapper.toAmenityResponse(amenity))
                .toList();
    }
}
