package com.nguyenvukhanhuygmail.shoppingonline.model;

import java.io.Serializable;

/**
 * Created by toannq on 11/9/2017.
 */

public class Product implements Serializable {

    private int product_id;
    private String product_name;
    private int product_price;
    private String product_image;
    private String product_description;
    private int orders;
    private double rate_point;
    private int category_id;

    public Product(int product_id, String product_name, int product_price, String product_image, String product_description, int orders, double rate_point, int category_id) {
        this.product_id = product_id;
        this.product_name = product_name;
        this.product_price = product_price;
        this.product_image = product_image;
        this.product_description = product_description;
        this.orders = orders;
        this.rate_point = rate_point;
        this.category_id = category_id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public Product setProduct_id(int product_id) {
        this.product_id = product_id;
        return this;
    }

    public String getProduct_name() {
        return product_name;
    }

    public Product setProduct_name(String product_name) {
        this.product_name = product_name;
        return this;
    }

    public int getProduct_price() {
        return product_price;
    }

    public Product setProduct_price(int product_price) {
        this.product_price = product_price;
        return this;
    }

    public String getProduct_image() {
        return product_image;
    }

    public Product setProduct_image(String product_image) {
        this.product_image = product_image;
        return this;
    }

    public String getProduct_description() {
        return product_description;
    }

    public Product setProduct_description(String product_description) {
        this.product_description = product_description;
        return this;
    }

    public int getOrders() {
        return orders;
    }

    public Product setOrders(int orders) {
        this.orders = orders;
        return this;
    }

    public double getRate_point() {
        return rate_point;
    }

    public Product setRate_point(double rate_point) {
        this.rate_point = rate_point;
        return this;
    }

    public int getCategory_id() {
        return category_id;
    }

    public Product setCategory_id(int category_id) {
        this.category_id = category_id;
        return this;
    }
}