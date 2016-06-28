package com.semesterdomain.semesterprojekt;

/**
 * The type EAPopulation represent a population of tours for the EA.
 */
public class EAPopulation {

    /**
     * The population of EAtours.
     */
    EATour[] EATours;

    /**
     * Instantiates a new Ea population with the given populationSize.
     *
     * @param populationSize the size of the pupulation for the EA.
     */
    public EAPopulation(int populationSize) {
        EATours = new EATour[populationSize];

        //Loop and create individuals
        for (int i = 0; i < populationSize(); i++) {
            EATour newEATour = new EATour();
            newEATour.generateIndividual();
            saveTour(i, newEATour);
        }
    }

    /**
     * Save a tour to the EATour Array.
     *
     * @param index  the index in which position the tour should be saved.
     * @param EATour the EATour Array where the tour should be saved to.
     */
    public void saveTour(int index, EATour EATour) {
        EATours[index] = EATour;
    }

    /**
     * Gets a tour from the pululation.
     *
     * @param index the index that should be returned.
     * @return the tour
     */
    public EATour getTour(int index) {
        return EATours[index];
    }

    /**
     * Gets the fittest tour of the population.
     *
     * @return the fittest tour of the population
     */

    public EATour getFittest() {
        EATour fittest = EATours[0];
        // Loop through individuals to find fittest
        for (int i = 1; i < populationSize(); i++) {
            if (fittest.getFitness() <= getTour(i).getFitness()) {
                fittest = getTour(i);
            }
        }
        return fittest;
    }

    /**
     * Gets the average distance of a single tour.
     *
     * @return the average distance of a the tour.
     */
    public double getAverageDistance() {
        //Loop through individuals to find fittest
        double averageDistance = 0;
        int completeDistance = 0;

        for (int i = 0; i < populationSize(); i++) {
            completeDistance += EATours[i].getDistance();
        }
        averageDistance = completeDistance / populationSize();
        return averageDistance;
    }

    /**
     * Gets the current size of the population array
     *
     * @return the size of the population array.
     */
    public int populationSize() {
        return EATours.length;
    }
}

