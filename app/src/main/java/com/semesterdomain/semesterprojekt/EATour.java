package com.semesterdomain.semesterprojekt;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;

/**
 * The type EATour represents a single shoppingtour through the shop.
 */
public class EATour {

    /**
     * The Tour array holds the tour of products.
     */
    private ArrayList tour = new ArrayList<Product>();

    /**
     * The Fitness for a tour the fitness describes how suited a tour is for the TSP. A vaule between 0 and 1.
     */
    private double fitness = 0;

    /**
     * The Distance of the tour. The way trough the shop to every shoppinglist item also including the cash register and the entrance.
     */
    private int distance = 0;

    /**
     * The constant fix portions for the entrance of the shop.
     */
    public static Product entrance = new Product(0, 0);

    /**
     * The constant fix portions for the cash register of the shop.
     */
    public static Product cashRegister = new Product(0, 50);

    /**
     * Instantiates a new blank (null) tour with the size of the number of products.
     */
    public EATour() {
        for (int i = 0; i < EATourManager.numberOfProducts(); i++) {
            tour.add(null);
        }
    }

    /**
     * Instantiates a new Eatour.
     *
     * @param tour the shoppinglist for which a tour should be created.
     */
    public EATour(ArrayList<Product> tour) {

        this.tour = tour;
    }

    /**
     * Generates a random individual by shuffling for the EA.
     */
    public void generateIndividual() {
        //Loop through all the destination products and add them to the tour
        for (int productIndex = 0; productIndex < EATourManager.numberOfProducts(); productIndex++) {
            setProductToTour(productIndex, EATourManager.getProduct(productIndex));
        }
        //Randomly reorder the tour
        Collections.shuffle(tour);

        this.tour.add(0, entrance);
        this.tour.add(cashRegister);
        //Log.d("LOG", "" + tourSize());
    }

    /**
     * Gets a product from the tour.
     *
     * @param tourPosition the index of the product that should be returned.
     * @return the product from tour searched by the index.
     */
    public Product getProductFromTour(int tourPosition) {

        return (Product) tour.get(tourPosition);
    }

    /**
     * Sets a product in a certain position within a tour.
     * The fitness and distance are also set to zero because the shoppingtour got altered.
     *
     * @param tourPosition the index of the tour where the products should be added to.
     * @param product      the product that should be added to the shoppingtour.
     */
    public void setProductToTour(int tourPosition, Product product) {
        tour.set(tourPosition, product);
        fitness = 0;
        distance = 0;
    }

    /**
     * Calculates the fitness for a shoppingtour.
     *
     * @return the fitness of the shoppingtour must be a value randing from 0 to 1.
     */
    public double getFitness() {
        if (fitness == 0) {
            fitness = 1 / (double) getDistance();
        }
        return fitness;
    }

    /**
     * Calculates the total distance of a shoppingtour.
     *
     * @return the distance of a shoppingtour.
     */
    public int getDistance() {
        if (distance == 0) {
            int tourDistance = 0;
            //Loop through the tour's products
            for (int productIndex = 0; productIndex < tourSize(); productIndex++) {
                //Get the product the EA is travelling from
                Product fromProduct = getProductFromTour(productIndex);
                //the Product the EA is travelling to
                Product destinationProduct = null;
                //Check we're not on our tour's last product, if we are set our
                //tour's final destination product to our starting product
                if (productIndex + 1 < tourSize()) {
                    destinationProduct = getProductFromTour(productIndex + 1);
                } else {
                    break;
                }
                //Get the distance between the two products
                tourDistance += fromProduct.getDistanceTo(destinationProduct);
            }
            distance = tourDistance;
        }
        return distance;
    }

    /**
     * Gets the number of products of a shoppingtour.
     *
     * @return the int size of the shoppingtour.
     */
    public int tourSize() {
        return tour.size();
    }

    /**
     * Check if the tour already contains a product.
     *
     * @param product the product that maybe already part of the shoppingtour.
     * @return the boolean indicates if the product is already part of the shoppingtour.
     */
    public boolean tourContainsProduct(Product product) {
        return tour.contains(product);
    }

    /**
     * Used to log the products of a shoppingtour.
     *
     * @return all products of a shoppingtour as a string.
     */
    @Override
    public String toString() {
        String geneString = "|";
        for (int i = 0; i < tourSize(); i++) {
            geneString += getProductFromTour(i) + "|";
        }
        return geneString;
    }

    /**
     * Gets a tour.
     *
     * @return the tour as an ArrayList
     */
    public ArrayList<Product> getTour() {
        return tour;
    }
}

