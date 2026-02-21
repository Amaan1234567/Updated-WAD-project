package com.products_service.services;

import com.products_service.repository.productRepository;
import com.products_service.entity.Product;
import com.products_service.entity.newProduct;
import com.products_service.entity.productType;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class productService {

    @Autowired
    private productRepository productRepository;

    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    public List<Product> getProductsByFilter(String name, Float min, Float max, Float disc, String wiki,
            productType type) {
        // Convert Enum to String name (e.g., productType.PLANT -> "PLANT")
        String typeString = (type != null) ? type.name() : null;
        
        if (name == "") {
            name = null;
        }
        return productRepository.searchProducts(name, min, max, disc, wiki, typeString);
    }

    public void insertProduct(newProduct newProductObj) {
        Product newProduct = new Product(newProductObj);
        
        productRepository.save(newProduct);
    }

    public ResponseEntity<String> updateProduct(Product updatedProduct) {
        try {
            Product currentProduct = productRepository.findById(updatedProduct.getId()).get();
            if (!updatedProduct.getName().isBlank()) {
                currentProduct.setName(updatedProduct.getName());
            } else {
                throw new Exception("name cannot be null");
            }

            if (!updatedProduct.getDescription().isBlank()) {
                currentProduct.setDescription(updatedProduct.getDescription());
            } else {
                throw new Exception("description cannot be null");
            }

            if (!updatedProduct.getWikiLink().isBlank()) {
                currentProduct.setWikiLink(updatedProduct.getWikiLink());
            } else {
                throw new Exception("wiki link cannot be null");
            }

            if (updatedProduct.getPrice() > 0) {
                currentProduct.setPrice(updatedProduct.getPrice());
            } else {
                throw new Exception("price cannot be <= 0");
            }

            if (updatedProduct.getDiscount() >= 0 && updatedProduct.getDiscount() < 100) {
                currentProduct.setDiscount(updatedProduct.getDiscount());
            } else {
                throw new Exception("discount cannot be < 0 and > 100");
            }

            currentProduct.setProductType(updatedProduct.getProductType());
            currentProduct.setResourceLinks(updatedProduct.getResouceLinks());

            productRepository.save(currentProduct);
            return ResponseEntity.ok("Updated record");

        } catch (IllegalArgumentException err) {
            System.out.println("Found null id");
            return ResponseEntity.badRequest().body("Found null id");
        } catch (NoSuchElementException err) {
            System.out.println("element not found");
            return ResponseEntity.status(HttpStatusCode.valueOf(404)).body("product record not found");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    
    public ResponseEntity<String> deleteProduct(Long id) {
        try {
            productRepository.delete(productRepository.findById(id).get());
            return ResponseEntity.ok("Product Deleted");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("found null id");
        } catch (NoSuchElementException err) {
            return ResponseEntity.status(HttpStatusCode.valueOf(404)).body("couldn't find record with id {" + id + "}");
        }
    }
}
