/*
    AUTHOR: enol826
    LICENSE: MIT
    SOURCE: GitHub (https://github.com/enol826/)
*/
package source;

import java.util.Random;
import source.IOManager.IOManager;

public class Config {
    private boolean config;

    private Object [] alphabet; // Encoding

    private double pCrossOver;
    private double pMutation;

    private int numGenerations;
    private int numIndividuals;
    private int chromoLength;

    private int elitismNumber;

    private IOManager manager;

    public Config(Object[] alphabet, double pCrossOver, double pMutation, int numGenerations, int numIndividuals, int chromoLength, int elitismNumber) {
        this(alphabet,pCrossOver,pMutation,numGenerations,numIndividuals,chromoLength,elitismNumber,null);
    }

    public Config(Object[] alphabet, double pCrossOver, double pMutation, int numGenerations, int numIndividuals, int chromoLength, int elitismNumber, String file) {
        if(numIndividuals % 2 != 0) throw new RuntimeException("Numbers of individuals must be a multiple of 2");
        if(elitismNumber % 2 != 0) throw new RuntimeException("Elitism number must be a multiple of 2");
        this.alphabet = alphabet;
        this.pCrossOver = pCrossOver;
        this.pMutation = pMutation;
        this.numGenerations = numGenerations;
        this.numIndividuals = numIndividuals;
        this.chromoLength = chromoLength;
        this.config = true;
        if(file == null) this.manager = null;
        else this.manager = new IOManager(file);
        this.elitismNumber = elitismNumber;
    }

    public Config() {
        this.config = false;
    }

    public boolean isConfig() {
        return this.config;
    }

    public boolean isFileProvided() {
        return this.manager != null;
    }

    public Object[] getAlphabet() {
        return this.alphabet;
    }

    public double getpCrossOver() {
        return this.pCrossOver;
    }

    public double getpMutation() {
        return this.pMutation;
    }

    public int getNumGenerations() {
        return this.numGenerations;
    }

    public int getNumIndividuals() {
        return this.numIndividuals;
    }

    public int getChromoLength() {
        return this.chromoLength;
    }

    public IOManager getIOManager() {
        return this.manager;
    }

    public int getElitismNumber() {
        return this.elitismNumber;
    }

    public static double getRandomNumber() {
        return new Random().nextDouble();
    }

    public int selectFromAlphabet() {
        double number = Config.getRandomNumber();
        int len = this.alphabet.length, i = 1;
        boolean found = false;
        while(i<len && !found) {
            if (number < (double) i / len){
                found = true;
            }else {
                i++;
            }
        }
        return i - 1;
    }
}
