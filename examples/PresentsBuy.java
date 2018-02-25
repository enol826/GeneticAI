/*
    AUTHOR: enol826
    LICENSE: MIT
    SOURCE: GitHub (https://github.com/enol826/)
*/
package examples;

import source.Algorithm;
import source.Config;

/*
    PROBLEM: An AI investigator wants to buy 5 presents.
    The information concercing price and rating on Amazon is given by the Present class. The 20 presents available are represented by
    the "presents" vector of the PresentBuy class.
    We want the sum of the price of the five presents to be as close as possible to 300 (the investigator's budget) and the difference
    betweeen the highest and the lowest price to be as small as possible.

    MODELING:
    - Chromosome of 5 genes, each of them representing the number of the present chosen in that Individual.
    - Fitness function: 1/(1+r+s)
    where
        r = abs(300 - sumOfPrices)
        s = maxPrice - minPrice
 */

class Present{
    private int price; // Price of the present
    private int rating; // Rating of the product in Amazon

    public Present(int price, int rating) {
        this.price = price;
        this.rating = rating;
    }

    public int getPrice() {
        return this.price;
    }
    public int getRating() {
        return this.rating;
    }
}

public class PresentsBuy extends Algorithm {
    private Present [] presents = new Present[20];

    public PresentsBuy() {
        this.setPresents();
    }

    public void setPresents() {
        int [] prices = {
                62,55,17,115,40,65,80,83,99,49,25,30,22,30,30,50,70,60,58,110
        };
        int [] ratings = {
                1,4,3,2,5,1,1,2,5,4,3,4,4,3,2,3,5,4,4,5
        };
        for(int i=0;i<presents.length;i++)
            this.presents[i] = new Present(prices[i],ratings[i]);
    }

    /* -------------- FITNESS FUNCTION -------------- */
    public int getMax(Object [] chromosome) {
        int max = Integer.MIN_VALUE;
        for(int i=0;i<chromosome.length;i++)
            if( this.presents[((Integer) chromosome[i]).intValue()].getPrice() >  max) max = this.presents[((Integer) chromosome[i]).intValue()].getPrice();
        return max;
    }

    public int getMin(Object [] chromosome) {
        int min = Integer.MAX_VALUE;
        for(int i=0;i<chromosome.length;i++)
            if( this.presents[((Integer) chromosome[i]).intValue()].getPrice() < min ) min = this.presents[((Integer) chromosome[i]).intValue()].getPrice();
        return min;
    }

    public double fitness(Object [] chromosome) {
        int totalCost = 0;
        double fitness = 0.0;
        int r, s;
        for(int i=0;i<chromosome.length;i++)
            totalCost += this.presents[((Integer) chromosome[i]).intValue()].getPrice();
        r = Math.abs(300-totalCost);
        s = this.getMax(chromosome) - this.getMin(chromosome);
        if(totalCost <= 300) {
            fitness = (double) 1/(1+r+s);
        }
        return fitness;
    }

    /* -------------------------------------------- */
    /* -------------------------------------------- */

    public static void main(String [] args) {
        Object [] alphabet = new Integer[20]; // Alphabet
        for(int i=0;i<20;i++) alphabet[i] = new Integer(i); // Generate alphabet; Representing the number of the present in the "presents" vector
        Config c = new Config(
                alphabet,0.6,0,20,400,5,10
        );
        PresentsBuy presentsBuy = new PresentsBuy();
        presentsBuy.setConfig(c);
        presentsBuy.init();
        presentsBuy.show();
        // presentsBuy.toFile(); /* You must provide a valid file */
    }

}
