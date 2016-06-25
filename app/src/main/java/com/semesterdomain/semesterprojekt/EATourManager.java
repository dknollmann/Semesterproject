package com.semesterdomain.semesterprojekt;

import java.util.ArrayList;

public class EATourManager {

    //Holds the products
    public static ArrayList<Product> destinationProducts = new ArrayList<Product>();

    //Adds a destination product
    public static void addProduct(Product product) {
        destinationProducts.add(product);
    }

    //Get a product
    public static Product getProduct(int index) {
        return (Product) destinationProducts.get(index);
    }

    //Get the number of destination products
    public static int numberOfProducts() {
        return destinationProducts.size();
    }

}
