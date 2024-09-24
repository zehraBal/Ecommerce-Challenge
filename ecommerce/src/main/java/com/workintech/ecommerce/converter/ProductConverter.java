package com.workintech.ecommerce.converter;

import com.workintech.ecommerce.dto.ProductResponse;
import com.workintech.ecommerce.entity.Product;

import java.util.List;
import java.util.stream.Collectors;

public class ProductConverter {
    public static ProductResponse convertToProductResponse(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getStockQuantity(),
                product.getPrice(),
                product.getCategory().getName()
        );
    }

    public static List<ProductResponse> convertToProductResponseList(List<Product> products) {
        return products.stream()
                .map(ProductConverter::convertToProductResponse)
                .collect(Collectors.toList());
    }
}
