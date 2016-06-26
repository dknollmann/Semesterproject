package com.semesterdomain.semesterprojekt;

import java.util.Random;

/**
 * The type Ea travling salesman.
 */
public class EATravlingSalesman {

    /**
     * The constant mutationRate.
     */
    private static final double mutationRate = 0.00001;

    /**
     * Selection ea population.
     *
     * @param pop the pop
     * @return the ea population
     */
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

    /**
     * Rand int int.
     *
     * @param min the min
     * @param max the max
     * @return the int
     */
    private static int randInt(int min, int max) {
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min)) + min;
        return randomNum;
    }

    /**
     * Mutate.
     *
     * @param EATour the ea tour
     */
//Mutate a EATour using swap mutation
    private static void mutate(EATour EATour) {
        //Loop through EATour products
        for (int tourPos1 = 1; tourPos1 < EATour.tourSize() - 1; tourPos1++) {
            //Apply mutation rate
            if (Math.random() < mutationRate) {
                //Get a second random position in the EATour
                //int tourPos2 = (int) ((EATour.tourSize() * Math.random()) -1);
                int tourPos2 = randInt(1, EATour.tourSize() - 1);
                //Get the products at target position in EATour

                Product product1 = EATour.getProductFromTour(tourPos1);
                Product product2 = EATour.getProductFromTour(tourPos2);

                //Swap them around
                EATour.setProductToTour(tourPos2, product1);
                EATour.setProductToTour(tourPos1, product2);
            }
        }
    }

    /**
     * Choose parent ea tour.
     *
     * @param pop the pop
     * @return the ea tour
     */
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
        //Create new child tour
        EATour child = new EATour();

        //Get start and end sub tour positions for parent1's tour
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

        //Loop and add the sub tour from parent1 to our child
        for (int i = startPos; endPos < child.tourSize(); i++) {
        	child.setCity(i, parent1.getCity(i));
        }

        //Loop through parent2's city tour
        for (int i = 0; i < parent2.tourSize(); i++) {
            //If child doesn't have the city add it
            if (!child.tourContainsProduct(parent2.getCity(i))) {
                //Loop to find a spare position in the child's tour
                for (int j = 0; j < child.tourSize(); j++) {
                    //Spare position found, add city
                    if (child.getCity(j) == null) {
                        child.setCity(j, parent2.getCity(i));
                        break;
                    }
                }
            }
        }
        return child;
    }*/


    /**
     * Crossover ea tour.
     *
     * @param parent1 the parent 1
     * @param parent2 the parent 2
     * @return the ea tour
     */
    public static EATour crossover(EATour parent1, EATour parent2) {
        //Create new child tour
        EATour child = new EATour();
        child.getTour().add(0, EATour.entrance);
        child.getTour().add(EATour.cashRegister);

        // Get start and end sub tour positions for parent1's tour

        int startPos = randInt(1, parent1.tourSize() - 1);
        int endPos = randInt(1, parent1.tourSize() - 1);
        // Loop and add the sub tour from parent1 to our child
        for (int i = 1; i < child.tourSize() - 1; i++) {
            // If our start position is less than the end position
            if (startPos < endPos && i > startPos && i < endPos) {
                child.setProductToTour(i, parent1.getProductFromTour(i));
            } // If our start position is larger
            else if (startPos > endPos) {
                if (!(i < startPos && i > endPos)) {
                    child.setProductToTour(i, parent1.getProductFromTour(i));
                }
            }
        }

        //Loop through parent2's product tour
        for (int i = 1; i < parent2.tourSize() - 1; i++) {
            // If child doesn't have the product add it
            if (!child.tourContainsProduct(parent2.getProductFromTour(i))) {
                //Loop to find a spare position in the child's tour
                for (int ii = 1; ii < child.tourSize() - 1; ii++) {
                    //Spare position found, add product
                    if (child.getProductFromTour(ii) == null) {
                        child.setProductToTour(ii, parent2.getProductFromTour(i));
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
