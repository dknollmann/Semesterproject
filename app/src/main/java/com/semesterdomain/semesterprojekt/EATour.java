package com.semesterdomain.semesterprojekt;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;

public class EATour {

    //Holds the tour of products
    private ArrayList tour = new ArrayList<Product>();

    private double fitness = 0;
    private int distance = 0;
    //fix portions for the entrance and the Cash Register
    public static Product entrance = new Product(0, 0);
    public static Product cashRegister = new Product(0, 50);

    //Constructs a blank tour
    public EATour() {
        for (int i = 0; i < EATourManager.numberOfProducts(); i++) {
            tour.add(null);
        }
    }

    public EATour(ArrayList<Product> tour) {

        this.tour = tour;
    }

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
        Log.d("LOG", "" + tourSize());
    }

    //Gets a product from the tour
    public Product getProductFromTour(int tourPosition) {

        return (Product) tour.get(tourPosition);
    }

    //Sets a product in a certain position within a tour
    public void setProductToTour(int tourPosition, Product product) {
        tour.set(tourPosition, product);
        //If the EATours been altered the fitness and distance need to be reseated
        fitness = 0;
        distance = 0;
    }

    //Gets the EATours fitness
    public double getFitness() {
        if (fitness == 0) {
            fitness = 1 / (double) getDistance();
        }
        return fitness;
    }

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
                tourDistance += fromProduct.distanceTo(destinationProduct);
            }
            distance = tourDistance;
        }
        return distance;
    }

    //Get number of products on our tour
    public int tourSize() {
        return tour.size();
    }

    // heck if the tour contains a product
    public boolean tourContainsProduct(Product product) {
        return tour.contains(product);
    }

    @Override
    public String toString() {
        String geneString = "|";
        for (int i = 0; i < tourSize(); i++) {
            geneString += getProductFromTour(i) + "|";
        }
        return geneString;
    }

    public ArrayList<Product> getTour() {
        return tour;
    }
}
