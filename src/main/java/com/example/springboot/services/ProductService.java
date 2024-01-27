package com.example.springboot.services;

import com.example.springboot.controllers.ProductController;
import com.example.springboot.dtos.ProductRecordDto;
import com.example.springboot.exceptions.ProductNotFoundException;
import com.example.springboot.exceptions.ProductsListEmptyException;
import com.example.springboot.models.ProductModel;
import com.example.springboot.repositories.ProductRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class ProductService {
    @Autowired
    ProductRepository productRepository;

    public ResponseEntity<ProductModel> saveProduct(ProductRecordDto productRecordDto) {
        var productModel = new ProductModel();
        BeanUtils.copyProperties(productRecordDto, productModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(productRepository.save(productModel));
    }

    public ResponseEntity<List<ProductModel>> getAllProducts() {
        List<ProductModel> productList = productRepository.findAll();
        if (!productList.isEmpty()) {
            for (ProductModel product : productList) {
                UUID id = product.getIdProduct();
                product.add(linkTo(methodOn(ProductController.class).getOneProduct(id)).withSelfRel());
            }
            return ResponseEntity.status(HttpStatus.OK).body(productList);
        }
        throw new ProductsListEmptyException("Products list is empty");
    }

    public ResponseEntity<ProductModel> getOneProduct(UUID id) {
        Optional<ProductModel> product = productRepository.findById(id);
        if (product.isPresent()) {
            product.get().add(linkTo(methodOn(ProductController.class).getAllProducts()).withRel("Products List."));
            return ResponseEntity.status(HttpStatus.OK).body(product.get());
        }
        throw new ProductNotFoundException(id.toString());
    }

    public ResponseEntity<ProductModel> updateProduct(UUID id, ProductRecordDto productRecordDto) {
        Optional<ProductModel> product = productRepository.findById(id);
        if (product.isPresent()) {
            var productModel = product.get();
            BeanUtils.copyProperties(productRecordDto, productModel);
            return ResponseEntity.status(HttpStatus.OK).body(productRepository.save(productModel));
        }
        throw new ProductNotFoundException(id.toString());
    }

    public ResponseEntity<String> deleteProduct(UUID id) {
        Optional<ProductModel> product = productRepository.findById(id);
        if (product.isPresent()) {
            productRepository.delete(product.get());
            return ResponseEntity.status(HttpStatus.OK).body("Product deleted successfully");
        }
        throw new ProductNotFoundException(id.toString());
    }
}
