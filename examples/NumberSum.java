package examples;

import source.Algorithm;
import source.Config;

public class NumberSum extends Algorithm {
    /*
        2x^2 + 7 = 0
        EXAMPLE.
        PROBLEM
        Given:
            1. array of N numbers (e.g. {2,5,9,10})
            2. array of M possible operations (e.g. {+,-,*})
            3. a expected result (e.g. 53)
            How can we combine the numbers and operations given in order to get the expected result
            We look for:
                2 [op1] 5 [op2] 9 [op3] 10 = 53
     */

    /* -------------- FITNESS FUNCTION -------------- */
    private int operation(int n1, int n2, String op) {
        switch(op) {
            case "+":
                return n1+n2;
            case "-":
                return n1-n2;
            case "*":
                return n1*n2;
            default:
                return 0;
        }
    }
    public double fitness(Object [] chromosome) {
        int [] numbers = {2,5,9,10};
        int c = 0;
        for(int i=0;i<numbers.length;i++) {
            if(i == 0) {
                c = operation(c,numbers[i],"+");
            }else{
                c = operation(c,numbers[i],(String) chromosome[i-1]);
            }
        }
        int d = Math.abs(53-c);
        return (double) 1/(1+d);
    }

    /* -------------------------------------------- */
    /* -------------------------------------------- */

    public static void main(String [] args) {
        Object [] alphabet = {"+","-","*"}; // Alphabet
        Config c = new Config(
                alphabet,0.8,0,15,100,3,0,"FILE PATH"
        );
        NumberSum numberSum = new NumberSum();
        numberSum.setConfig(c);
        numberSum.init();
        numberSum.show();
    }

}
