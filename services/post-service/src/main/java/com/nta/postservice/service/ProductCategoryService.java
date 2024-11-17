package com.nta.postservice.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.nta.postservice.entity.ProductCategory;
import com.nta.postservice.repository.ProductCategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductCategoryService {
    private final ProductCategoryRepository productCategoryRepository;

    public List<ProductCategory> findAll() {
        return productCategoryRepository.findAll();
    }
}
