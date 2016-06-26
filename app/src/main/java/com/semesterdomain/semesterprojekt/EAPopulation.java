package com.semesterdomain.semesterprojekt;

/**
 * The type Ea population.
 */
public class EAPopulation {

    /**
     * The Ea tours.
     */
//Holds population of EATours
    EATour[] EATours;

    /**
     * Instantiates a new Ea population.
     *
     * @param populationSize the population size
     */
//Construct a population
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
     * Save tour.
     *
     * @param index  the index
     * @param EATour the ea tour
     */
//Saves a EATour
    public void saveTour(int index, EATour EATour) {
        EATours[index] = EATour;
    }

    /**
     * Gets tour.
     *
     * @param index the index
     * @return the tour
     */
//Gets a tour from population
    public EATour getTour(int index) {
        return EATours[index];
    }

    /**
     * Gets fittest.
     *
     * @return the fittest
     */
//Gets the best tour in the population
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
     * Gets average distance.
     *
     * @return the average distance
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
     * Population size int.
     *
     * @return the int
     */
// Gets population size
    public int populationSize() {
        return EATours.length;
    }
}
