package com.semesterdomain.semesterprojekt;

/**
 * Created by L 875 on 22.06.2016.
 */
public class Population {

    // Holds population of tours
    Tour[] tours;

    // Construct a population
    public Population(int populationSize) {
        tours = new Tour[populationSize];

        // Loop and create individuals
        for (int i = 0; i < populationSize(); i++) {
            Tour newTour = new Tour();
            newTour.generateIndividual();
            saveTour(i, newTour);
        }
    }

    // Saves a tour
    public void saveTour(int index, Tour tour) {
        tours[index] = tour;
    }

    // Gets a tour from population
    public Tour getTour(int index) {
        return tours[index];
    }

    // Gets the best tour in the population
    public Tour getFittest() {
        Tour fittest = tours[0];
        // Loop through individuals to find fittest
        for (int i = 1; i < populationSize(); i++) {
            if (fittest.getFitness() <= getTour(i).getFitness()) {
                fittest = getTour(i);
            }
        }
        return fittest;
    }
    public double getAverageDistance() {
        // Loop through individuals to find fittest
        double averageDistance = 0;
        int completeDistance = 0;

        for (int i = 0; i < populationSize(); i++) {
            completeDistance += tours[i].getDistance();
        }
        averageDistance = completeDistance/populationSize();
        return averageDistance;
    }

    // Gets population size
    public int populationSize() {
        return tours.length;
    }
}
