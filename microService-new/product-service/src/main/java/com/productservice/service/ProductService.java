package com.productservice.service;

import com.productservice.dtos.ProductRequest;
import com.productservice.dtos.ProductResponse;

import java.util.List;

public interface ProductService {


    public String createProduct(ProductRequest productRequest);

    public List<ProductResponse> getAllProducts();

}
