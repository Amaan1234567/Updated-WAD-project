package com.products_service.entity;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Table(name = "products",schema = "products_svc", indexes = {
        @Index(name = "product_id_index", columnList = "id")
})
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "product_id_generator")
    @SequenceGenerator(name = "product_id_generator", sequenceName = "id_gen", // The actual name in the DB
            schema = "products_svc", // The schema with dot needs escaped quotes
            allocationSize = 10)
    @Column(name = "product_id")
    private Long id;
    @Column(name = "product_name", nullable = false)
    private String name;

    @Column(name = "product_description")
    private String description;

    @Column(name = "product_wiki_link")
    private String wikiLink;
    
    
    @Column(name = "product_resource_links",nullable=false)
    private List<String> resourceLinks;

    @Column(name = "price", nullable = false)
    private float price;

    @Column(name = "discount", nullable = false)
    private float discount;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_type", columnDefinition = "products_svc.product_type", nullable = false)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private productType productType;

    public Product() {
        // Default constructor required by JPA
    }

    public Product(Long id, String name, String description, float price, float discount, String wikiLink,
            productType productType) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.discount = discount;
        this.wikiLink = wikiLink;
        this.productType = productType;
    }

    public Product(newProduct newProduct) {
        this.name = newProduct.getName();
        this.description = newProduct.getDescription();
        this.price = newProduct.getPrice();
        this.wikiLink = newProduct.getWikiLink();
        this.resourceLinks = newProduct.getResourceLinks();
        this.discount = newProduct.getDiscount();
        this.productType = newProduct.getProductType();
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getPrice() {
        return this.price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getDiscount() {
        return this.discount;
    }

    public void setDiscount(float discount) {
        this.discount = discount;
    }

    public productType getProductType() {
        return this.productType;
    }

    public void setProductType(productType productType) {
        this.productType = productType;
    }

    public String getWikiLink() {
        return this.wikiLink;
    }

    public void setWikiLink(String newLink) {
        this.wikiLink = newLink;
    }

    public List<String> getResouceLinks() {
        return this.resourceLinks;
    }

    public void setResourceLinks(List<String> resourceLinks) {
        this.resourceLinks = resourceLinks;
    }

    public String toString() {
        return """
            {
                "id":%d
                        "name":%s,
                        "Description":%s,
                        "wikiLink":%s,
                        "resourceLinks":%o,
                        "price": %f,
                        "discount": %f,
                        "productType" : %o
            }
                """.formatted(this.id,
                this.name,
                this.description,
                this.wikiLink,
                this.resourceLinks,
                this.price,
                this.discount,
                this.productType != null ? this.productType.name() : "null");
    }
}