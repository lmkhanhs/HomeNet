package com.lmkhanhs.home_net.services;

import java.util.List;

import org.hibernate.sql.Update;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.lmkhanhs.home_net.context.TenantContext;
import com.lmkhanhs.home_net.dtos.categories.requests.CreateCategoryRequest;
import com.lmkhanhs.home_net.dtos.categories.requests.UpdateCategoryRequest;
import com.lmkhanhs.home_net.dtos.categories.responses.CategoriesResponse;
import com.lmkhanhs.home_net.entities.CategoryEntity;
import com.lmkhanhs.home_net.exceptions.AppException;
import com.lmkhanhs.home_net.exceptions.ErrorCode;
import com.lmkhanhs.home_net.mappers.CategoryMapper;
import com.lmkhanhs.home_net.repositories.CategoryRepository;

import lombok.RequiredArgsConstructor;
import lombok.val;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class CategoryService {
    CategoryRepository categoryRepository;
    CategoryMapper categoryMapper;
    //create category
    public CategoriesResponse createCategory(CreateCategoryRequest createCategoryRequest) {
        String tenantId = TenantContext.getTenant();
        // check if category name already exists
        this.categoryRepository.findByNameAndTenantId(createCategoryRequest.getName(), tenantId)
            .ifPresent(category -> {
                throw new AppException(ErrorCode.CATEGORY_EXISTED, "try another name category");
            });
        var categoryEntity = categoryMapper.toCategoryEntity(createCategoryRequest);
        categoryEntity.setTenantId(tenantId);
        categoryEntity.setPosition((int) this.categoryRepository.count());
        categoryEntity = categoryRepository.save(categoryEntity);

        return categoryMapper.toCategoryResponse(categoryEntity);
    }
    // update category
    public CategoriesResponse updateCategory(Long categoryId, UpdateCategoryRequest updateCategoryRequest) {
        String tenantId = TenantContext.getTenant();
        var categoryEntity = this.categoryRepository.findByIdAndTenantIdAndIsDeletedFalse(categoryId, tenantId)
            .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND, "category not found"));
        // check if category name already exists
        this.categoryRepository.findByNameAndTenantId(updateCategoryRequest.getName(), tenantId)
            .ifPresent(category -> {
                if (!category.getId().equals(categoryId)) {
                    throw new AppException(ErrorCode.CATEGORY_EXISTED, "try another name category " + updateCategoryRequest.getName() + " is existed");
                }
            });
        categoryMapper.updateCategoryEntityFromRequest(updateCategoryRequest, categoryEntity);
        categoryEntity = categoryRepository.save(categoryEntity);
        return categoryMapper.toCategoryResponse(categoryEntity);
    }
    
    // get category by page
    public List<CategoriesResponse> getAllCategories(Pageable pageable) {
        String tenantId = TenantContext.getTenant();
        List<CategoryEntity> categoryEntities = this.categoryRepository.findAllByTenantIdAndIsDeletedFalse(tenantId, pageable).getContent();
        return categoryEntities.stream()
            .map(categoryMapper::toCategoryResponse)
            .toList();
    }

    // get category by id
    public CategoriesResponse getCategoryById(Long categoryId) {
        String tenantId = TenantContext.getTenant();
        var categoryEntity = this.categoryRepository.findByIdAndTenantIdAndIsDeletedFalse(categoryId, tenantId)
            .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND, "category not found"));
        return categoryMapper.toCategoryResponse(categoryEntity);
    }

    // delete category
    public void deleteCategory(Long categoryId) {
        String tenantId = TenantContext.getTenant();
        var categoryEntity = this.categoryRepository.findByIdAndTenantIdAndIsDeletedFalse(categoryId, tenantId)
            .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND, "category not found"));
        categoryEntity.setIsDeleted(true);
        this.categoryRepository.save(categoryEntity);
    }

    // restore category
    public void restoreCategory(Long categoryId) {  
        String tenantId = TenantContext.getTenant();
        var categoryEntity = this.categoryRepository.findByIdAndTenantIdAndIsDeletedTrue(categoryId, tenantId)
            .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND, "category not found"));
        categoryEntity.setIsDeleted(false);
        this.categoryRepository.save(categoryEntity);
    }
}
