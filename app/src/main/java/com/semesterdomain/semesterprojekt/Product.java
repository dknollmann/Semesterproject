package com.semesterdomain.semesterprojekt;

import java.io.Serializable;

public class Product implements Serializable {

    private long productId;
    private String productName;
    private String manufacturer;
    private long price;
    private double posX;
    private double posY;


    //eigentlich nutzlos nochmal schauen ;)
    public Product(String name, String manufacturer, long price) {
        this.productName = name;
        this.manufacturer = manufacturer;
        this.price = price;
    }

    //constructor for entrance and cash register
    public Product(int posx, int posy) {
        this.posX = posx;
        this.posY = posy;
        this.productName = "dummy";
        this.manufacturer = "dummyManufacturer";
        this.price = 0;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long product_id) {
        this.productId = product_id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public double getX() {
        return posX;
    }

    public void setPosX(double posX) {
        this.posX = posX;
    }

    public double getY() {
        return posY;
    }

    public void setPosY(double posY) {
        this.posY = posY;
    }

    @Override
    public String toString() {
        return String.valueOf(productId);
    }

    // Gets the distance to given city
    public double getDistanceTo(Product product) {
        double xDistance = Math.abs(getX() - product.getX());
        double yDistance = Math.abs(getY() - product.getY());
        double distance = Math.sqrt((xDistance * xDistance) + (yDistance * yDistance));

        return distance;
    }

}
