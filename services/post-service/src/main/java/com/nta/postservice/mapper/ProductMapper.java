package com.nta.postservice.mapper;

import org.mapstruct.Mapper;

import com.nta.postservice.dto.request.ProductCreationRequest;
import com.nta.postservice.dto.response.ProductResponse;
import com.nta.postservice.entity.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductResponse toProductResponse(Product request);

    Product toProduct(ProductCreationRequest request);
}
