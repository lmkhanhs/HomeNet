package com.lmkhanhs.home_net.mappers;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import com.lmkhanhs.home_net.dtos.amenities.requests.CreateAmenityRequest;
import com.lmkhanhs.home_net.dtos.amenities.requests.UpdateAmenityRequest;
import com.lmkhanhs.home_net.dtos.amenities.responses.AmenityResponse;
import com.lmkhanhs.home_net.entities.AmenityEntity;
import com.lmkhanhs.home_net.utils.UploadImageUtills;

@Mapper(componentModel = "spring")
public abstract class AmenityMapper {
    @Autowired
    UploadImageUtills uploadImageUtills;
    public abstract AmenityResponse toAmenityResponse(AmenityEntity amenityEntity);

    // to ENTITTY
    @Mapping(target = "iconUrl", source = "icon", qualifiedByName = "mapIconUrl")
    public abstract AmenityEntity toAmenityEntity(CreateAmenityRequest amenityRequest);

    @Named("mapIconUrl")
    protected String mapIconUrl(MultipartFile icon) {
        if (icon == null ) {
            return null;
        }
        return this.uploadImageUtills.uploadAsUrl(icon);
    }
    // update entity
    @Mapping(target = "iconUrl", source = "icon", qualifiedByName = "mapIconUrl")
    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE)
    public abstract void updateAmenityEntity(UpdateAmenityRequest updateAmenityRequest, @MappingTarget AmenityEntity amenityEntity);
}