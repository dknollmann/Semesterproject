package com.semesterdomain.semesterprojekt;

import java.util.ArrayList;

/**
 * The type Ea tour manager.
 */
public class EATourManager {

    /**
     * The constant destinationProducts.
     */
//Holds the products
    public static ArrayList<Product> destinationProducts = new ArrayList<Product>();

    /**
     * Add product.
     *
     * @param product the product
     */
//Adds a destination product
    public static void addProduct(Product product) {
        destinationProducts.add(product);
    }

    /**
     * Gets product.
     *
     * @param index the index
     * @return the product
     */
//Get a product
    public static Product getProduct(int index) {
        return (Product) destinationProducts.get(index);
    }

    /**
     * Number of products int.
     *
     * @return the int
     */
//Get the number of destination products
    public static int numberOfProducts() {
        return destinationProducts.size();
    }

}
