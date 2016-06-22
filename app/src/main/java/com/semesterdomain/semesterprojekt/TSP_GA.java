package com.semesterdomain.semesterprojekt;

import android.util.Log;

import java.util.Random;

/**
 * Created by L 875 on 22.06.2016.
 */

public class TSP_GA {

        private static final double mutationRate = 0.00001;

        public static Population selection (Population pop){

            Tour[] newTours = new Tour[pop.populationSize()];

            for(int i = 0; i < pop.populationSize(); i++){

                Tour firstParent = chooseParent(pop);
                Tour secondParent = chooseParent(pop);
                Tour child = crossover(firstParent, secondParent);

                mutate(child);
                newTours[i] = child;
            }
            //System.out.print("	" + "	" + pop.getFittest().getDistance() + "	"+ (int) pop.getAverageDistance() + "\n");

            for(int j = 0; j < pop.populationSize(); j++){
                pop.saveTour(j, newTours[j]);
            }


            return pop;
        }

        private static int randInt(int min, int max){
            Random rand = new Random();
            int randomNum = rand.nextInt((max - min)) + min;
            return randomNum;
        }

        // Mutate a tour using swap mutation
        private static void mutate(Tour tour) {
            // Loop through tour cities
            for(int tourPos1=1; tourPos1 < tour.tourSize()-1; tourPos1++){
                // Apply mutation rate
                if(Math.random() < mutationRate){
                    // Get a second random position in the tour
                    //int tourPos2 = (int) ((tour.tourSize() * Math.random()) -1);
                    int tourPos2 = randInt(1, tour.tourSize() - 1);
                    // Get the cities at target position in tour

                    Product product1 = tour.getProduct(tourPos1);
                    Product product2 = tour.getProduct(tourPos2);

                    // Swap them around
                    tour.setProduct(tourPos2, product1);
                    tour.setProduct(tourPos1, product2);
                }
            }
        }
        public static Tour chooseParent(Population pop){

            double selection = Math.random();
            double fitness = 0;

            double percent = 0;

            double fitnessAll = 0;
            for(int i = 0; i < pop.populationSize(); i++){
                fitnessAll += pop.tours[i].getFitness();
            }


            for(int i = 0; i < pop.populationSize(); i++){
                percent += pop.tours[i].getFitness()/fitnessAll;
                //fitness += pop.tours[i].getFitness();
                //Log.d("LOG", "4");
                if(selection <= percent){
                    //Log.d("LOG", "return");
                    return pop.getTour(i);
                }
            }
            //fail
            return null;
        }

	/*public static Tour crossover(Tour parent1, Tour parent2) {
        // Create new child tour
        Tour child = new Tour();

        // Get start and end sub tour positions for parent1's tour
        int startPos;
        int endPos;

        int number1 = (int) (Math.random() * parent1.tourSize());
        int number2 = (int) (Math.random() * parent1.tourSize());

        if(number1 <= number2){
        	startPos = number1;
        	endPos = number2;
        }else{
        	startPos = number2;
        	endPos = number1;
        }

        // Loop and add the sub tour from parent1 to our child
        for (int i = startPos; endPos < child.tourSize(); i++) {
        	child.setCity(i, parent1.getCity(i));
        }

        // Loop through parent2's city tour
        for (int i = 0; i < parent2.tourSize(); i++) {
            // If child doesn't have the city add it
            if (!child.containsProduct(parent2.getCity(i))) {
                // Loop to find a spare position in the child's tour
                for (int j = 0; j < child.tourSize(); j++) {
                    // Spare position found, add city
                    if (child.getCity(j) == null) {
                        child.setCity(j, parent2.getCity(i));
                        break;
                    }
                }
            }
        }
        return child;
    }*/


        public static Tour crossover(Tour parent1, Tour parent2) {
            // Create new child tour
            Tour child = new Tour();
            // Get start and end sub tour positions for parent1's tour
            /*int startPos = (int) (Math.random() * parent1.tourSize());
            int endPos = (int) (Math.random() * parent1.tourSize());
*/
            int startPos = randInt(1, parent1.tourSize() - 1);
            int endPos = randInt(1, parent1.tourSize() - 1);
            // Loop and add the sub tour from parent1 to our child
            for (int i = 1; i < child.tourSize() - 1; i++) {
                // If our start position is less than the end position
                if (startPos < endPos && i > startPos && i < endPos) {
                    child.setProduct(i, parent1.getProduct(i));
                } // If our start position is larger
                else if (startPos > endPos) {
                    if (!(i < startPos && i > endPos)) {
                        child.setProduct(i, parent1.getProduct(i));
                    }
                }
            }

            // Loop through parent2's product tour
            for (int i = 1; i < parent2.tourSize() - 1; i++) {
                // If child doesn't have the product add it
                if (!child.containsProduct(parent2.getProduct(i))) {
                    // Loop to find a spare position in the child's tour
                    for (int ii = 1; ii < child.tourSize() -1; ii++) {
                        // Spare position found, add product
                        if (child.getProduct(ii) == null) {
                            child.setProduct(ii, parent2.getProduct(i));
                            break;
                        }
                    }
                }
            }
            return child;
        }


}
