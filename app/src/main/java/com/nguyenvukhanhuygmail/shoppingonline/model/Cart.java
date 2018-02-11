package com.nguyenvukhanhuygmail.shoppingonline.model;

import java.io.Serializable;

/**
 * Created by toannq on 11/24/2017.
 */

public class Cart implements Serializable {

    private int product_id;
    private String product_name;
    private long product_price;
    private String product_image;
    private long product_number;
    private long limit_num;

    public Cart(int product_id, String product_name, long product_price, String product_image, long product_number, long limit_num) {
        this.product_id = product_id;
        this.product_name = product_name;
        this.product_price = product_price;
        this.product_image = product_image;
        this.product_number = product_number;
        this.limit_num = limit_num;
    }

    public int getProduct_id() {
        return product_id;
    }

    public Cart setProduct_id(int product_id) {
        this.product_id = product_id;
        return this;
    }

    public String getProduct_name() {
        return product_name;
    }

    public Cart setProduct_name(String product_name) {
        this.product_name = product_name;
        return this;
    }

    public long getProduct_price() {
        return product_price;
    }

    public Cart setProduct_price(long product_price) {
        this.product_price = product_price;
        return this;
    }

    public String getProduct_image() {
        return product_image;
    }

    public Cart setProduct_image(String product_image) {
        this.product_image = product_image;
        return this;
    }

    public long getProduct_number() {
        return product_number;
    }

    public Cart setProduct_number(long product_number) {
        this.product_number = product_number;
        return this;
    }

    public long getLimit_num() {
        return limit_num;
    }

    public Cart setLimit_num(long limit_num) {
        this.limit_num = limit_num;
        return this;
    }
}

