package com.semesterdomain.semesterprojekt;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;

/**
 * The type Ea tour.
 */
public class EATour {

    /**
     * The Tour.
     */
//Holds the tour of products
    private ArrayList tour = new ArrayList<Product>();

    /**
     * The Fitness.
     */
    private double fitness = 0;
    /**
     * The Distance.
     */
    private int distance = 0;
    /**
     * The constant entrance.
     */
//fix portions for the entrance and the Cash Register
    public static Product entrance = new Product(0, 0);
    /**
     * The constant cashRegister.
     */
    public static Product cashRegister = new Product(0, 50);

    /**
     * Instantiates a new Ea tour.
     */
//Constructs a blank tour
    public EATour() {
        for (int i = 0; i < EATourManager.numberOfProducts(); i++) {
            tour.add(null);
        }
    }

    /**
     * Instantiates a new Ea tour.
     *
     * @param tour the tour
     */
    public EATour(ArrayList<Product> tour) {

        this.tour = tour;
    }

    /**
     * Generate individual.
     */
// Creates a random individual
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
     * Gets product from tour.
     *
     * @param tourPosition the tour position
     * @return the product from tour
     */
//Gets a product from the tour
    public Product getProductFromTour(int tourPosition) {

        return (Product) tour.get(tourPosition);
    }

    /**
     * Sets product to tour.
     *
     * @param tourPosition the tour position
     * @param product      the product
     */
//Sets a product in a certain position within a tour
    public void setProductToTour(int tourPosition, Product product) {
        tour.set(tourPosition, product);
        //If the EATours been altered the fitness and distance need to be reseated
        fitness = 0;
        distance = 0;
    }

    /**
     * Gets fitness.
     *
     * @return the fitness
     */
//Gets the EATours fitness
    public double getFitness() {
        if (fitness == 0) {
            fitness = 1 / (double) getDistance();
        }
        return fitness;
    }

    /**
     * Gets distance.
     *
     * @return the distance
     */
//Gets the total distance of the tour
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
                // tour's final destination product to our starting product
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
     * Tour size int.
     *
     * @return the int
     */
//Get number of products on our tour
    public int tourSize() {
        return tour.size();
    }

    /**
     * Tour contains product boolean.
     *
     * @param product the product
     * @return the boolean
     */
// heck if the tour contains a product
    public boolean tourContainsProduct(Product product) {
        return tour.contains(product);
    }

    /**
     * To string string.
     *
     * @return the string
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
     * Gets tour.
     *
     * @return the tour
     */
    public ArrayList<Product> getTour() {
        return tour;
    }
}
