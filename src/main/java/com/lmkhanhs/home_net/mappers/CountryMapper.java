package com.lmkhanhs.home_net.mappers;

import java.util.Objects;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import com.lmkhanhs.home_net.dtos.countries.requests.CreateCountryRequest;
import com.lmkhanhs.home_net.dtos.countries.requests.UpdateCountryRequest;
import com.lmkhanhs.home_net.dtos.countries.responses.CountryResponse;
import com.lmkhanhs.home_net.entities.CountryEntity;
import com.lmkhanhs.home_net.utils.UploadImageUtills;

@Mapper(componentModel = "spring")
public abstract class CountryMapper {
    @Autowired
    private UploadImageUtills imageUtills;

    public abstract CountryResponse toResponse(CountryEntity entity);

    @Mapping(source = "thumnail", target = "thumbnailUrl", qualifiedByName = "mapImageUrl")
    public abstract CountryEntity toEntity(CreateCountryRequest request);
    
    @Named("mapImageUrl")
    protected String mapImageUrl(MultipartFile file){
        if (Objects.isNull(file)) {
            return null;
        }
        return this.imageUtills.uploadAsUrl(file);
    }
    // update entity from request
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source =  "thumbnail", target = "thumbnailUrl", qualifiedByName = "mapImageUrl")
    @Mapping(target = "id", ignore = true)
    public abstract void updateEntityFromRequest( UpdateCountryRequest request, @MappingTarget CountryEntity entity);
}
