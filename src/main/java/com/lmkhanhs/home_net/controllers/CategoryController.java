package com.lmkhanhs.home_net.controllers;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.lmkhanhs.home_net.dtos.apps.ApiResponse;
import com.lmkhanhs.home_net.dtos.categories.requests.CreateCategoryRequest;
import com.lmkhanhs.home_net.dtos.categories.requests.UpdateCategoryRequest;
import com.lmkhanhs.home_net.dtos.categories.responses.CategoriesResponse;
import com.lmkhanhs.home_net.services.CategoryService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("${app.prefix}/categories")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryController {
    CategoryService categoryService;
    // create 
    @PreAuthorize("hasAuthority('CREATE_CATEGORY')")
    @ResponseStatus(org.springframework.http.HttpStatus.CREATED)    
    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<CategoriesResponse> createCategory(@ModelAttribute CreateCategoryRequest createCategoryRequest) {
        return ApiResponse.<CategoriesResponse>builder()
            .code(201)
            .message("Create category successfully!")
            .data(categoryService.createCategory(createCategoryRequest))
            .build();
    }
    // update category
    @PreAuthorize("hasAuthority('UPDATE_CATEGORY')")
    @PutMapping(value = "/{categoryId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<CategoriesResponse> updateCategory(@PathVariable String categoryId, @ModelAttribute UpdateCategoryRequest request) {
        Long categoryIdLong = Long.parseLong(categoryId);
        return ApiResponse.<CategoriesResponse>builder()
            .code(200)
            .message("Update category successfully!")
            .data(categoryService.updateCategory(categoryIdLong, request))
            .build();
    }
    // delete category
    @PreAuthorize("hasAuthority('DELETE_CATEGORY')")
    @DeleteMapping(value = "/{categoryId}")
    public ApiResponse<Void> deleteCategory(@PathVariable String categoryId) {
        Long categoryIdLong = Long.parseLong(categoryId);
        this.categoryService.deleteCategory(categoryIdLong);
        return ApiResponse.<Void>builder()
            .code(200)
            .message("Delete category successfully!")
            .build();
    }
    // restore category
    @PreAuthorize("hasAuthority('RESTORE_CATEGORY')")
    @PutMapping(value = "/{categoryId}/restore")
    public ApiResponse<Void> restoreCategory(@PathVariable String categoryId) {
        Long categoryIdLong = Long.parseLong(categoryId);
        this.categoryService.restoreCategory(categoryIdLong);
        return ApiResponse.<Void>builder()
            .code(200)
            .message("Restore category successfully!")
            .build();
    }
    // get all categories by pageable
    @GetMapping("")
    public ApiResponse<List<CategoriesResponse>> getCategories(Pageable pageable) {
        return ApiResponse.<List<CategoriesResponse>>builder()
            .code(200)
            .message("Get categories successfully!")
            .data(
                this.categoryService.getAllCategories(pageable)
            )
            .build();
    }
    // get category by id
    @GetMapping("/{categoryId}")
    public ApiResponse<CategoriesResponse> getCategoryById(@PathVariable String categoryId) {
        Long categoryIdLong = Long.parseLong(categoryId);
        return ApiResponse.<CategoriesResponse>builder()
            .code(200)
            .message("Get category successfully!")
            .data(
                this.categoryService.getCategoryById(categoryIdLong)
            )
            .build();
    }
}

