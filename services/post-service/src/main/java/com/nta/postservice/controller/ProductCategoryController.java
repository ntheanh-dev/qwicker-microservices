package com.nta.postservice.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nta.postservice.dto.response.ApiResponse;
import com.nta.postservice.entity.ProductCategory;
import com.nta.postservice.service.ProductCategoryService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/product-category")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductCategoryController {
    ProductCategoryService productCategoryService;

    @GetMapping
    ApiResponse<List<ProductCategory>> findAllProductCategories() {
        return ApiResponse.<List<ProductCategory>>builder()
                .result(productCategoryService.findAll())
                .build();
    }
}
