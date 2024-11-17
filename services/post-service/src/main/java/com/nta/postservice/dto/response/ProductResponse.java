package com.nta.postservice.dto.response;

import com.nta.postservice.entity.ProductCategory;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductResponse {

    int quantity;
    String image;
    String mass;
    ProductCategory productCategory;
}
