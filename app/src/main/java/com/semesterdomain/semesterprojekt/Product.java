package com.semesterdomain.semesterprojekt;

import java.io.Serializable;

public class Product implements Serializable{

    private long product_id;
    private String productname;
    private String manufacturer;
    private long price;
    private double posX;
    private double posY;

    public Product (String name, String manufacturer, long price){
        this.productname = name;
        this.manufacturer = manufacturer;
        this.price = price;
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

    public double getPosX() {
        return posX;
    }

    public void setPosX(double posX) {
        this.posX = posX;
    }

    public double getPosY() {
        return posY;
    }

    public void setPosY(double posY) {
        this.posY = posY;
    }

    @Override
    public String toString(){
        return ""+product_id;
    }

}
