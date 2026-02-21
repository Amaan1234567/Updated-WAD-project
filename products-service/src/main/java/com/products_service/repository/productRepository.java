package com.products_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.products_service.entity.Product;

public interface productRepository extends JpaRepository<Product, Long>{
    
    @Query(value = "SELECT * FROM products_svc.products p WHERE " +
            "(:name IS NULL OR p.product_name ILIKE CONCAT('%', :name, '%')) AND " +
            "(:minPrice IS NULL OR p.price >= :minPrice) AND " +
            "(:maxPrice IS NULL OR p.price <= :maxPrice) AND " +
            "(:minDiscount IS NULL OR p.discount >= :minDiscount) AND " +
            "(:wikiLink IS NULL OR p.product_wiki_link ILIKE CONCAT('%', :wikiLink, '%')) AND " +
            "(:type IS NULL OR p.product_type = CAST(CAST(:type AS text) AS products_svc.product_type))", nativeQuery = true)
    List<Product> searchProducts(
        @Param("name") String name,
        @Param("minPrice") Float minPrice,
        @Param("maxPrice") Float maxPrice,
        @Param("minDiscount") Float minDiscount,
        @Param("wikiLink") String wikiLink,
        @Param("type") String type
    );
}
