package com.lmkhanhs.home_net.services;

import java.util.List;

import org.hibernate.query.Page;
import org.hibernate.sql.Update;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.lmkhanhs.home_net.context.TenantContext;
import com.lmkhanhs.home_net.dtos.countries.requests.CreateCountryRequest;
import com.lmkhanhs.home_net.dtos.countries.requests.UpdateCountryRequest;
import com.lmkhanhs.home_net.dtos.countries.responses.CountryResponse;
import com.lmkhanhs.home_net.entities.CountryEntity;
import com.lmkhanhs.home_net.exceptions.AppException;
import com.lmkhanhs.home_net.exceptions.ErrorCode;
import com.lmkhanhs.home_net.mappers.CountryMapper;
import com.lmkhanhs.home_net.repositories.CountryRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CountryService {
    CountryRepository countryRepository;
    CountryMapper countryMapper;
    
    public CountryResponse handleCreateCountry(CreateCountryRequest request){
        String tenantuId =  TenantContext.getTenant();
        this.countryRepository.findByNameAndTenantId(request.getName(), tenantuId)
            .ifPresent( country -> {
                throw new AppException(ErrorCode.COUNTRY_EXISTED, "Try another country name");
            });
        CountryEntity countryEntity = this.countryMapper.toEntity(request);
        countryEntity.setTenantId(tenantuId);
        countryEntity.setPosition( (int) this.countryRepository.count() );
        return this.countryMapper.toResponse(
            this.countryRepository.save(countryEntity)
        );
    }

    public List<CountryResponse> handleGetAllCountries(Pageable pageable){
        String tenantId = TenantContext.getTenant();
        List<CountryEntity> countries = this.countryRepository.findAllByTenantIdAndIsDeletedFalse(tenantId, pageable).getContent();
        return countries.stream()
            .map( country -> this.countryMapper.toResponse(country))
            .toList();
    }

    // handle updagte country
    public CountryResponse handleUpdateCountry(Long countryId, UpdateCountryRequest request){
        String tenantId = TenantContext.getTenant();
        CountryEntity countryEntity = this.countryRepository.findByIdAndTenantIdAndIsDeletedFalse(countryId, tenantId)
            .orElseThrow( () -> new AppException(ErrorCode.COUNTRY_NOT_FOUND, "Try another country id"));
        this.countryMapper.updateEntityFromRequest(request, countryEntity);
        return this.countryMapper.toResponse(
            this.countryRepository.save(countryEntity)
        );
    }

    // handle delete country
    public void handleDeleteCountry(Long countryId){
        String tenantId = TenantContext.getTenant();
        CountryEntity countryEntity = this.countryRepository.findByIdAndTenantIdAndIsDeletedFalse(countryId, tenantId)
            .orElseThrow( () -> new AppException(ErrorCode.COUNTRY_NOT_FOUND, "Try another country id"));
        countryEntity.setIsDeleted(true);
        this.countryRepository.save(countryEntity);
    }

    // handle restore country
    public void handleRestoreCountry(Long countryId){
        String tenantId = TenantContext.getTenant();
        CountryEntity countryEntity = this.countryRepository.findByIdAndTenantIdAndIsDeletedTrue(countryId, tenantId)
            .orElseThrow( () -> new AppException(ErrorCode.COUNTRY_NOT_FOUND, "Try another country id"));
        countryEntity.setIsDeleted(false);
        this.countryRepository.save(countryEntity);
    }

    // handle get country by id
    public CountryResponse handleGetCountryById(Long countryId){
        String tenantId = TenantContext.getTenant();
        CountryEntity countryEntity = this.countryRepository.findByIdAndTenantIdAndIsDeletedFalse(countryId, tenantId)
            .orElseThrow( () -> new AppException(ErrorCode.COUNTRY_NOT_FOUND, "Try another country id"));
        return this.countryMapper.toResponse(countryEntity);
    }
}
