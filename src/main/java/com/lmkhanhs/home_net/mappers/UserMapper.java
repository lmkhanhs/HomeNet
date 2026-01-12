package com.lmkhanhs.home_net.mappers;

import java.util.Set;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import com.lmkhanhs.home_net.dtos.countries.responses.CountryResponse;
import com.lmkhanhs.home_net.dtos.users.requests.CreateUserRequest;
import com.lmkhanhs.home_net.dtos.users.requests.UpdateProfileUser;
import com.lmkhanhs.home_net.dtos.users.responses.PermissionResponse;
import com.lmkhanhs.home_net.dtos.users.responses.RoleResponse;
import com.lmkhanhs.home_net.dtos.users.responses.UserResponse;
import com.lmkhanhs.home_net.entities.CountryEntity;
import com.lmkhanhs.home_net.entities.PermissionEntity;
import com.lmkhanhs.home_net.entities.RoleEntity;
import com.lmkhanhs.home_net.entities.UserEntity;
import com.lmkhanhs.home_net.repositories.CountryRepository;
import com.lmkhanhs.home_net.utils.UploadImageUtills;

@Mapper(componentModel = "spring")
public abstract class UserMapper {
    @Autowired
    UploadImageUtills uploadImageUtills;
    @Autowired
    CountryRepository countryRepository;
    @Autowired
    CountryMapper countryMapper;

    public abstract UserEntity toEntity(CreateUserRequest request);

    @Mapping(source = "roles", target = "roles", qualifiedByName = "toRolesResponse")
    @Mapping(target = "country", source = "country", qualifiedByName = "mapCountryEntityToResponse")
    public abstract UserResponse toResponse(UserEntity entity);
    @Named("mapCountryEntityToResponse")
    protected CountryResponse mapCountryEntityToResponse(CountryEntity country) {
        if (country == null) {
            return null;
        }
        return countryMapper.toResponse(country);
    }

    @Named("toRolesResponse")
    protected Set<RoleResponse> toRolesResponse(Set<RoleEntity> entities) {
        Helper helper = new Helper();
        Set<RoleResponse> responses = new java.util.HashSet<>();
        for (RoleEntity entity : entities) {
            responses.add(helper.toRoleResponse(entity));
        }
        return responses;
    }
    // update profile
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "username", ignore = true)
    @Mapping(target = "avatarUrl", source = "avatar", qualifiedByName = "mapAvatarUrl")
    @Mapping(target = "country", source = "countryId", qualifiedByName = "mapCountry")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract void updateProfileFromDto(UpdateProfileUser dto, @MappingTarget UserEntity entity);

    @Named("mapAvatarUrl")
    protected String mapAvatarUrl(MultipartFile avatar) {
        if (avatar == null) {
            return null;
        }
        return uploadImageUtills.uploadAsUrl(avatar);
    }
    @Named("mapCountry")
    protected com.lmkhanhs.home_net.entities.CountryEntity mapCountry(String countryId) {
        if (countryId == null) {
            return null;
        }
        return countryRepository.findById(Long.parseLong(countryId)).orElse(null);
    }
}

class Helper {
    
    public PermissionResponse toPermissionResponse(PermissionEntity entity) {
        PermissionResponse response = new PermissionResponse();
        response.setName(entity.getName());
        response.setDescription(entity.getDescription());
        response.setPermissionId(Long.toString(entity.getId()));
        return response;
    }   
    public RoleResponse toRoleResponse(RoleEntity entity) {
        RoleResponse response = new RoleResponse();
        response.setName(entity.getName());
        response.setRoleId(Long.toString(entity.getId()));
        java.util.Set<PermissionResponse> permissionResponses = new java.util.HashSet<>();
        for (PermissionEntity permissionEntity : entity.getPermissions()) {
            permissionResponses.add(toPermissionResponse(permissionEntity));
        }
        response.setPermissions(permissionResponses);
        return response;
    }
}