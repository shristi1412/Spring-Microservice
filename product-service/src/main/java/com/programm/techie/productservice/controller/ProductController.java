package com.programm.techie.productservice.controller;

import com.programm.techie.productservice.dto.ProductRequest;
import com.programm.techie.productservice.dto.ProductResponse;
import com.programm.techie.productservice.model.Product;
import com.programm.techie.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createProduct(@RequestBody ProductRequest productRequest){
        productService.createProduct(productRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.CREATED)
    public List<ProductResponse> getAllproducts(){
        return productService.getAllproducts();
    }
}
