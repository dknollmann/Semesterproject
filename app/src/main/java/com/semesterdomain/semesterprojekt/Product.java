package com.semesterdomain.semesterprojekt;

import java.io.Serializable;

public class Product implements Serializable {

    private long product_id;
    private String productname;
    private String manufacturer;
    private long price;
    private double posX;
    private double posY;


    //eigentlich nutzlos nochmal schauen ;)
    public Product(String name, String manufacturer, long price) {
        this.productname = name;
        this.manufacturer = manufacturer;
        this.price = price;
    }

    //constructor for entrance and cash register
    public Product(int posx, int posy) {
        this.posX = posx;
        this.posY = posy;
        this.productname = "dummy";
        this.manufacturer = "dummyManufacturer";
        this.price = 0;
    }

    public long getProduct_id() {
        return product_id;
    }

    public void setProduct_id(long product_id) {
        this.product_id = product_id;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
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
        return "" + product_id;
    }

    // Gets the distance to given city
    public double distanceTo(Product product) {
        double xDistance = Math.abs(getX() - product.getX());
        double yDistance = Math.abs(getY() - product.getY());
        double distance = Math.sqrt((xDistance * xDistance) + (yDistance * yDistance));

        return distance;
    }

}
