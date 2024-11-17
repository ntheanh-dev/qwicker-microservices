package com.nta.postservice.configuration;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.nta.postservice.entity.ProductCategory;
import com.nta.postservice.repository.ProductCategoryRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {
    ProductCategoryRepository productCategoryRepository;

    @Bean
    @ConditionalOnProperty(
            prefix = "spring",
            value = "datasource.driverClassName",
            havingValue = "com.mysql.cj.jdbc.Driver")
    ApplicationRunner initApplicationRunner(ProductCategoryRepository productCategoryRepository) {
        return args -> {
            log.info("Initializing application...");
            log.info("Initializing product category");
            if (productCategoryRepository.count() == 0) {
                productCategoryRepository.save(
                        ProductCategory.builder().name("Thực phẩm & đồ uống").build());
                productCategoryRepository.save(
                        ProductCategory.builder().name("Văn phòng phẩm").build());
                productCategoryRepository.save(
                        ProductCategory.builder().name("Quần áo & Phụ kiện").build());
                productCategoryRepository.save(
                        ProductCategory.builder().name("Đồ điện tử").build());
                productCategoryRepository.save(ProductCategory.builder()
                        .name("Nguyên liệu / Linh kiện")
                        .build());
                productCategoryRepository.save(
                        ProductCategory.builder().name("Đồ gia dụng / Nội thất").build());
            }
        };
    }
}
