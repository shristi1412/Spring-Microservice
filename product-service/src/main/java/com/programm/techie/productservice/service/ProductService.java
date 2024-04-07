package com.programm.techie.productservice.service;

import com.programm.techie.productservice.dto.ProductRequest;
import com.programm.techie.productservice.dto.ProductResponse;
import com.programm.techie.productservice.model.Product;
import com.programm.techie.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public void createProduct(ProductRequest productRequest) {
        Product product = Product.builder().name(productRequest.getName()).
                description(productRequest.getDescription()).
                price(productRequest.getPrice()).build();

        productRepository.save(product);
    }

    public List<ProductResponse> getAllproducts() {
         List<Product> products = productRepository.findAll();
         return  products.stream().map(this::mapProductToProducrResponse).toList();
    }

    private ProductResponse mapProductToProducrResponse(Product product) {
       return  ProductResponse.builder().id(product.getId()).
                name(product.getName()).description(product.getDescription())
                .price(product.getPrice()).build();
    }
}
