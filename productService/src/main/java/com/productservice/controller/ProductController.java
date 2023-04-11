package com.productservice.controller;


import com.productservice.dtos.ProductRequest;
import com.productservice.dtos.ProductResponse;
import com.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {


    private final ProductService productService;

    @PostMapping
    public ResponseEntity<String> createProduct(@RequestBody ProductRequest productRequest){

        return  new ResponseEntity<>(productService.createProduct(productRequest) , HttpStatus.CREATED);

    }



    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts(){
        return  new ResponseEntity<>(productService.getAllProducts(), HttpStatus.OK);
    }




}
