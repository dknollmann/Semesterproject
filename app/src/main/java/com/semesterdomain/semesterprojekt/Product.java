package com.semesterdomain.semesterprojekt;

import java.io.Serializable;

/**
 * The type Product.
 */
public class Product implements Serializable {

    /**
     * The Product id.
     */
    private long productId;
    /**
     * The Product name.
     */
    private String productName;
    /**
     * The Manufacturer.
     */
    private String manufacturer;
    /**
     * The Price.
     */
    private long price;
    /**
     * The Pos x.
     */
    private double posX;
    /**
     * The Pos y.
     */
    private double posY;


    /**
     * Instantiates a new Product.
     *
     * @param name         the name
     * @param manufacturer the manufacturer
     * @param price        the price
     */
//eigentlich nutzlos nochmal schauen ;)
    public Product(String name, String manufacturer, long price) {
        this.productName = name;
        this.manufacturer = manufacturer;
        this.price = price;
    }

    /**
     * Instantiates a new Product.
     *
     * @param posx the posx
     * @param posy the posy
     */
//constructor for entrance and cash register
    public Product(int posx, int posy) {
        this.posX = posx;
        this.posY = posy;
        this.productName = "dummy";
        this.manufacturer = "dummyManufacturer";
        this.price = 0;
    }

    /**
     * Gets product id.
     *
     * @return the product id
     */
    public long getProductId() {
        return productId;
    }

    /**
     * Sets product id.
     *
     * @param product_id the product id
     */
    public void setProductId(long product_id) {
        this.productId = product_id;
    }

    /**
     * Gets product name.
     *
     * @return the product name
     */
    public String getProductName() {
        return productName;
    }

    /**
     * Sets product name.
     *
     * @param productName the product name
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }

    /**
     * Gets manufacturer.
     *
     * @return the manufacturer
     */
    public String getManufacturer() {
        return manufacturer;
    }

    /**
     * Sets manufacturer.
     *
     * @param manufacturer the manufacturer
     */
    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    /**
     * Gets price.
     *
     * @return the price
     */
    public long getPrice() {
        return price;
    }

    /**
     * Sets price.
     *
     * @param price the price
     */
    public void setPrice(long price) {
        this.price = price;
    }

    /**
     * Gets x.
     *
     * @return the x
     */
    public double getX() {
        return posX;
    }

    /**
     * Sets pos x.
     *
     * @param posX the pos x
     */
    public void setPosX(double posX) {
        this.posX = posX;
    }

    /**
     * Gets y.
     *
     * @return the y
     */
    public double getY() {
        return posY;
    }

    /**
     * Sets pos y.
     *
     * @param posY the pos y
     */
    public void setPosY(double posY) {
        this.posY = posY;
    }

    /**
     * To string string.
     *
     * @return the string
     */
    @Override
    public String toString() {
        return String.valueOf(productId);
    }

    /**
     * Gets distance to.
     *
     * @param product the product
     * @return the distance to
     */
// Gets the distance to given city
    public double getDistanceTo(Product product) {
        double xDistance = Math.abs(getX() - product.getX());
        double yDistance = Math.abs(getY() - product.getY());
        double distance = Math.sqrt((xDistance * xDistance) + (yDistance * yDistance));

        return distance;
    }

}
