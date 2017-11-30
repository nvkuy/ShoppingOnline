package com.nguyenvukhanhuygmail.shoppingonline.model;

/**
 * Created by toannq on 11/5/2017.
 */

public class Category {
    private int id;
    private String categoty_name;
    private String category_image;

    public Category(int id, String categoty_name, String category_image) {
        this.id = id;
        this.categoty_name = categoty_name;
        this.category_image = category_image;
    }

    public int getId() {
        return id;
    }

    public Category setId(int id) {
        this.id = id;
        return this;
    }

    public String getCategoty_name() {
        return categoty_name;
    }

    public Category setCategoty_name(String categoty_name) {
        this.categoty_name = categoty_name;
        return this;
    }

    public String getCategory_image() {
        return category_image;
    }

    public Category setCategory_image(String category_image) {
        this.category_image = category_image;
        return this;
    }
}
