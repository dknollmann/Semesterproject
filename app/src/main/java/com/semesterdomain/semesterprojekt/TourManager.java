package com.semesterdomain.semesterprojekt;

/**
 * Created by L 875 on 22.06.2016.
 */
/*
* TourManager.java
* Holds the products of a tour
*/


import java.util.ArrayList;

public class TourManager {

    // Holds our products
    public static ArrayList<Product> destinationProducts = new ArrayList<Product>();

    // Adds a destination product
    public static void addProduct(Product product) {
        destinationProducts.add(product);
    }

    // Get a product
    public static Product getProduct(int index){
        return (Product) destinationProducts.get(index);
    }

    // Get the number of destination products
    public static int numberOfProducts(){
        return destinationProducts.size();
    }

}
