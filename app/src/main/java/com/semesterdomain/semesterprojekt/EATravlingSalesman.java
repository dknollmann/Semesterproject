package com.semesterdomain.semesterprojekt;

import java.util.Random;

/**
 * Created by L 875 on 22.06.2016.
 */

public class EATravlingSalesman {

    private static final double mutationRate = 0.00001;

    public static EAPopulation selection(EAPopulation pop) {

        EATour[] newEATours = new EATour[pop.populationSize()];

        for (int i = 0; i < pop.populationSize(); i++) {

            EATour firstParent = chooseParent(pop);
            EATour secondParent = chooseParent(pop);
            EATour child = crossover(firstParent, secondParent);

            mutate(child);
            newEATours[i] = child;
        }
        //System.out.print("	" + "	" + pop.getFittest().getDistance() + "	"+ (int) pop.getAverageDistance() + "\n");

        for (int j = 0; j < pop.populationSize(); j++) {
            pop.saveTour(j, newEATours[j]);
        }


        return pop;
    }

    private static int randInt(int min, int max) {
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min)) + min;
        return randomNum;
    }

    // Mutate a EATour using swap mutation
    private static void mutate(EATour EATour) {
        // Loop through EATour cities
        for (int tourPos1 = 1; tourPos1 < EATour.tourSize() - 1; tourPos1++) {
            // Apply mutation rate
            if (Math.random() < mutationRate) {
                // Get a second random position in the EATour
                //int tourPos2 = (int) ((EATour.tourSize() * Math.random()) -1);
                int tourPos2 = randInt(1, EATour.tourSize() - 1);
                // Get the cities at target position in EATour

                Product product1 = EATour.getProduct(tourPos1);
                Product product2 = EATour.getProduct(tourPos2);

                // Swap them around
                EATour.setProduct(tourPos2, product1);
                EATour.setProduct(tourPos1, product2);
            }
        }
    }

    public static EATour chooseParent(EAPopulation pop) {

        double selection = Math.random();
        double fitness = 0;

        double percent = 0;

        double fitnessAll = 0;
        for (int i = 0; i < pop.populationSize(); i++) {
            fitnessAll += pop.EATours[i].getFitness();
        }


        for (int i = 0; i < pop.populationSize(); i++) {
            percent += pop.EATours[i].getFitness() / fitnessAll;
            //fitness += pop.EATours[i].getFitness();
            //Log.d("LOG", "4");
            if (selection <= percent) {
                //Log.d("LOG", "return");
                return pop.getTour(i);
            }
        }
        //fail
        return null;
    }

	/*public static EATour crossover(EATour parent1, EATour parent2) {
        // Create new child tour
        EATour child = new EATour();

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


    public static EATour crossover(EATour parent1, EATour parent2) {
        // Create new child tour
        EATour child = new EATour();
        child.getTour().add(0, EATour.entrance);
        child.getTour().add(EATour.cashRegister);

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
                for (int ii = 1; ii < child.tourSize() - 1; ii++) {
                    // Spare position found, add product
                    if (child.getProduct(ii) == null) {
                        child.setProduct(ii, parent2.getProduct(i));
                        break;
                    }
                }
            }
        }
        //child.getTour().add(0, EATour.entrance);
        //child.getTour().add(child.getTour().size()-1, EATour.cashRegister);
        return child;
    }


}
