package com.nguyenvukhanhuygmail.shoppingonline.model;

/**
 * Created by toannq on 2/14/2018.
 */

public class Contact {

    private String title;
    private String snippet;
    private String phone;
    private double lat;
    private double lng;
    private String email;
    private String address;

    public Contact() {
    }

    public Contact(String title, String snippet, String phone, double lat, double lng, String email, String address) {
        this.title = title;
        this.snippet = snippet;
        this.phone = phone;
        this.lat = lat;
        this.lng = lng;
        this.email = email;
        this.address = address;
    }

    public String getTitle() {
        return title;
    }

    public Contact setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getSnippet() {
        return snippet;
    }

    public Contact setSnippet(String snippet) {
        this.snippet = snippet;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public Contact setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public double getLat() {
        return lat;
    }

    public Contact setLat(double lat) {
        this.lat = lat;
        return this;
    }

    public double getLng() {
        return lng;
    }

    public Contact setLng(double lng) {
        this.lng = lng;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public Contact setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public Contact setAddress(String address) {
        this.address = address;
        return this;
    }
}
