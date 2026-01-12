package com.lmkhanhs.home_net.mappers;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import com.lmkhanhs.home_net.dtos.categories.requests.CreateCategoryRequest;
import com.lmkhanhs.home_net.dtos.categories.requests.UpdateCategoryRequest;
import com.lmkhanhs.home_net.dtos.categories.responses.CategoriesResponse;
import com.lmkhanhs.home_net.entities.CategoryEntity;
import com.lmkhanhs.home_net.utils.UploadImageUtills;

@Mapper(componentModel = "spring")
public abstract class CategoryMapper {
    @Autowired
    UploadImageUtills uploadImageUtills;
  
    public abstract CategoriesResponse toCategoryResponse(CategoryEntity categoryEntity);
    
    // convert CreateCategoryRequest to CategoryEntity
    @Mapping(target = "iconUrl", source = "icon", qualifiedByName = "mapIconToIconUrl")
    public abstract CategoryEntity toCategoryEntity(CreateCategoryRequest createCategoryRequest);

    // map icon MultipartFile to iconUrl String
    @Named("mapIconToIconUrl")
    protected String mapIconToIconUrl(MultipartFile icon) {
        if (icon == null || icon.isEmpty()) {
            return null;
        }
        return uploadImageUtills.uploadAsUrl(icon);
    }
    // update CategoryEntity from UpdateCategoryRequest
    @Mapping(target = "iconUrl", source = "icon", qualifiedByName = "mapIconToIconUrl")
    @BeanMapping(nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    public abstract void updateCategoryEntityFromRequest(UpdateCategoryRequest updateCategoryRequest, @MappingTarget CategoryEntity categoryEntity);    
    
}
