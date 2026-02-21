package com.products_service.entity;

public class SearchQuery {
    private String name;
    private float start_price;
    private float end_price;
    private float discount;
    private String wikiLink;
    private productType type;

    public SearchQuery() {
        // Default constructor
    }

    public SearchQuery(String name, float start_price, float end_price, float discount, String wikiLink,
            productType type) {
        this.name = name;
        this.start_price = start_price;
        this.end_price = end_price;
        this.discount = discount;
        this.wikiLink = wikiLink;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getStart_price() {
        return start_price;
    }

    public void setStart_price(float start_price) {
        this.start_price = start_price;
    }

    public float getEnd_price() {
        return end_price;
    }

    public void setEnd_price(float end_price) {
        this.end_price = end_price;
    }

    public float getDiscount() {
        return discount;
    }

    public void setDiscount(float discount) {
        this.discount = discount;
    }

    public String getWikiLink() {
        return this.wikiLink;
    }

    public void setWikiLink(String newWikiLink) {
        this.wikiLink = newWikiLink;
    }

    public productType getType() {
        return type;
    }

    public void setType(productType type) {
        this.type = type;
    }
}
