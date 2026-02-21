package com.products_service.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.products_service.entity.Product;
import com.products_service.entity.SearchQuery;
import com.products_service.entity.newProduct;
import com.products_service.entity.productType;
import com.products_service.services.productService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/products")
public class productsController {

    @Autowired
    productService productService;

    @GetMapping("/{id}")
    public ResponseEntity<String> getProductById(@PathVariable Long id) {

        Product product = productService.getProductById(id);
        if (product == null) {
            return ResponseEntity.status(404).body("product with id " + id + " not found");
        }
        ObjectMapper objectMapper = new ObjectMapper();
        String productAsString="";
        try {
            productAsString = objectMapper.writeValueAsString(product);
            System.out.println("Resulting JSON string: " + productAsString);
        } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(productAsString);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestBody SearchQuery query) {

        String name = query.getName();
        float start_price = query.getStart_price();
        float end_price = query.getEnd_price();
        float discount = query.getDiscount();
        String wikiLink = query.getWikiLink();

        List<Product> products = productService.getProductsByFilter(name, start_price, end_price, discount, wikiLink,
                productType.PLANT);
        return ResponseEntity.ok(products);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addProduct(@RequestBody newProduct product) {
        productService.insertProduct(product);
        return ResponseEntity.ok("Product added successfully");
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateProduct(@RequestBody Product updatedProduct) {
        return productService.updateProduct(updatedProduct);
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        return productService.deleteProduct(id);
    }
}
