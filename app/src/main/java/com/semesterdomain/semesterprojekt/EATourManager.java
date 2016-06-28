package com.semesterdomain.semesterprojekt;

import java.util.ArrayList;

/**
 * The type EATourManager is a helper class for the tours of the EA.
 */
public class EATourManager {

    /**
     * The constant destinationProducts holds products.
     */

    public static ArrayList<Product> destinationProducts = new ArrayList<Product>();

    /**
     * Adds a product to the destinationProducts.
     *
     * @param product the product that should be added.
     */
    public static void addProduct(Product product) {
        destinationProducts.add(product);
    }

    /**
     * Gets a products of the  destinationProducts array.
     *
     * @param index the index which should be returned
     * @return the product at the index position
     */
    public static Product getProduct(int index) {
        return (Product) destinationProducts.get(index);
    }

    /**
     * Gets the size of the destinationProducts.
     *
     * @return the size of the destinationProducts.
     */
    public static int numberOfProducts() {
        return destinationProducts.size();
    }

}

