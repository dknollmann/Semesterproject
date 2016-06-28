package com.semesterdomain.semesterprojekt;

import java.util.Random;

/**
 * The type EATravlingSalesman implements the evolutionary algorithm (EA) for a TSP.
 */
public class EATravlingSalesman {

    /**
     * The constant mutationRate if used to determine if a element of a shoppingtour should be mutated.
     */
    private static final double mutationRate = 0.00001;

    /**
     * The selection picks parents from the population for the childs of the next generation
     * with the chooseParent method. And generates the new childs with the crossover method.
     * After all childs are generated the childs replace the parents to creat the new population.
     *
     * @param pop the population of parents which were childs before.
     * @return the new population of child which become the new parents
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
     * Generates a random int in range of parameters.
     *
     * @param min the minimum range for the int.
     * @param max the maximum range for the int.
     * @return the randomly generated int.
     */
    private static int randInt(int min, int max) {
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min)) + min;
        return randomNum;
    }

    /**
     * Mutates a signle shoppingtour. The mutation swaps random positions of the shoppingtour around.
     * This method also adds the entrance and cash register after the mutation done.
     *
     * @param EATour the ea tour
     */
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
     * Chooses a tour as a parent for the childs from the population based on the fitness.
     *
     * @param pop the population where the parent should be choosen from.
     * @return the choosen tour.
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
        //no parent picked -> problem
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
     * Sets the elements of a new child based on the paramenters which are perant for the child.
     * The choosen elements from the perants are placed in between the last and first postion of the child.
     * This is done so that the entrance and the cash register do not get replaced and mess up the oder of elements.
     * It has also has to be considered that the first and last postion of the perant should not be picked as
     * elements for the child.
     *
     * @param parent1 the first parent where elements are choosen from.
     * @param parent2 the secound parent where elements are choosen from.
     * @return the ea tour
     */
    public static EATour crossover(EATour parent1, EATour parent2) {
        //Create new child tour
        EATour child = new EATour();
        child.getTour().add(0, EATour.entrance);
        child.getTour().add(EATour.cashRegister);

        //Get start and end sub tour positions for parent1's tour

        int startPos = randInt(1, parent1.tourSize() - 1);
        int endPos = randInt(1, parent1.tourSize() - 1);
        //Loop and add the sub tour from parent1 to our child
        for (int i = 1; i < child.tourSize() - 1; i++) {
            //If our start position is less than the end position
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
            //If child doesn't have the product add it
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

