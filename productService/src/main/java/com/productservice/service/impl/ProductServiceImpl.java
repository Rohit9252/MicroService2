package com.productservice.service.impl;

import com.productservice.dtos.ProductRequest;
import com.productservice.dtos.ProductResponse;
import com.productservice.model.Product;
import com.productservice.repository.ProductRepository;
import com.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class ProductServiceImpl implements ProductService {


    private  final ProductRepository productRepository;


    @Override
    public String createProduct(ProductRequest productRequest) {

        Product product = Product.builder()
                .nane(productRequest.getName())
                .price(productRequest.getPrice())
                .description(productRequest.getDescription())
                .build();


        log.info("Product Save to the database");

        productRepository.save(product);

        return "Product Created SuccessFully";

    }

    @Override
    public List<ProductResponse> getAllProducts() {
        log.info("Product Save to the database");
       return  productRepository.findAll()
               .stream()
               .map(this::mapToProduct)
               .collect(Collectors.toList());
    }


    public ProductResponse mapToProduct(Product product){

        ProductResponse productResponse = ProductResponse.builder()
                .id(product.getId())
                .nane(product.getNane())
                .price(product.getPrice())
                .description(product.getDescription())
                .build();



            return productResponse;
    }

}
