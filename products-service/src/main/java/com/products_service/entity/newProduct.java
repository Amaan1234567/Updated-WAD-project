package com.products_service.entity;

import java.util.List;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public class newProduct {

    private String name;
    private String description;
    private String wikiLink;
    private List<String> resourceLinks;
    private float price;
    private float discount = 0;

    @Enumerated(EnumType.STRING)
    private productType productType; // Changed from product_type

    public newProduct() {
        // Default constructor required by JPA
    }

    public newProduct(String name, String description, float price, float discount, String wikiLink,List<String> resourceLinks, productType productType) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.discount = discount;
        this.wikiLink = wikiLink;
        this.resourceLinks = resourceLinks;
        this.productType = productType;
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

    public List<String> getResourceLinks() {
        return this.resourceLinks;
    }

    public void setResourceLinks(List<String> resourceLinks) {
        this.resourceLinks = resourceLinks;
    }
}
