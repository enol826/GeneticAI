package source;

import java.util.Date;

public abstract class Algorithm{
    private Generation [] generations;
    private Config config;

    public Algorithm(Config config) {
        this.setConfig(config);
    }
    public Algorithm() {
        this.setConfig(new Config());
    }

    public void setConfig(Config config) {
        this.config = config;
        this.generations = new Generation[this.config.getNumGenerations()];
    }

    public void init() {
        if(!this.config.isConfig()) throw new RuntimeException("You need to set a Config class");
        for (int i = 0; i < this.generations.length; i++) {
            this.generations[i] = new Generation(i);
            if (i == 0) {
                this.generations[i].generatePopulation();
            } else {
                this.generations[i].setIndividuals(this.generations[i - 1].getIndividuals());
            }
            this.generations[i].process();
        }
    }

    public void show() {
        System.out.println(this.toString());
    }

    public void toFile() {
        if(!config.isFileProvided()) throw new RuntimeException("You must provide a valid file in the Config object passed");
        config.getIOManager().append(this.toString(),2);
    }

    public String toString() {
        Date date = new Date();
        StringBuilder s = new StringBuilder("EXECUTION ("+date.toString()+") \n");
        for(int i=0;i<this.generations.length;i++) {
            s.append("Generation: " + i + "\n");
            s.append(generations[i].toString());
        }
        return s.toString();
    }

    /* Custom methods. */
    public abstract double fitness(Object [] chromosome);

    /*
    ##########################################################
        CLASS GENERATION
    ##########################################################
    */
    protected class Generation {
        private Individual [] individuals;
        private double fitnessSum;
        private double [] probs;
        private Individual [] elitism;

        private int number;

        protected Generation(int number) {
            this.number = number;
            this.individuals = new Individual[config.getNumIndividuals()];
            this.probs = new double[config.getNumIndividuals()];
            this.elitism = new Individual[config.getElitismNumber()];
            for(int i=0;i<this.elitism.length;i++) elitism[i] = new Individual(0);
            this.fitnessSum = 0;
        }

        protected Individual[] getIndividuals() {
            return individuals;
        }

        protected void setIndividuals(Individual[] individuals) {
            this.individuals = individuals;
        }

        protected void generatePopulation() {
            for(int i=0;i<this.individuals.length;i++) {
                individuals[i] = new Individual();
                individuals[i].generate();
            }
        }

        protected double getFitnessSum() {
            return this.fitnessSum;
        }

        public String toString() {
            StringBuilder s = new StringBuilder("Fitness: "+this.getFitnessSum()+"\n");
            for(int i=0;i<this.individuals.length;i++)
                s.append("Individual: "+this.individuals[i].toString()+" : " +this.individuals[i].getFitness()+"\n");
            return s.toString();
        }

        // Stantard selection: depending on individual's prob
        private Individual selection() {
            boolean found = false;
            double number = Config.getRandomNumber();
            int i = 0;
            while (!found && i < this.probs.length) {
                if (number < this.probs[i]) found = true;
                else i++;
            }
            return this.individuals[i];
        }

        private void setFitness() {
            for(int i=0;i<this.individuals.length;i++) {
                this.individuals[i].fitness();
                this.fitnessSum += this.individuals[i].getFitness();
            }
            if(this.fitnessSum <= 0) throw new RuntimeException("Fitness function not valid: the sum of all fitnesses is <= 0.");
        }

        private void setProbs() {
            double probability;
            for(int i=0;i<this.individuals.length;i++) {
                 probability = this.individuals[i].getFitness() / this.fitnessSum;
                if(i == 0) this.probs[i] = probability;
                else this.probs[i] = this.probs[i-1] + probability;
            }
        }

        /* Processing fitness and probabilities of each generation */
        protected void process() {
            Individual [] individuals = new Individual[config.getNumIndividuals()];
            int i = config.getElitismNumber();
            this.setFitness();
            this.setProbs();

            // Add elitism first - TO DO

            // Selection, crossover and mutation
            while(i < this.individuals.length) {
                Individual i1 = selection();
                Individual i2 = selection();
                if(Config.getRandomNumber() < config.getpCrossOver()) i1.crossOver(i2);
                i1.mutation();
                i2.mutation();

                individuals[i] = i1;
                individuals[i+1] = i2;
                i += 2;
            }
            this.setIndividuals(individuals);

        }
        /*
        ##########################################################
            CLASS INDIVIDUAL
        ##########################################################
        */
        protected class Individual {
            private Object[] chromosome;
            private double fitness;

            protected Individual() {
                this.chromosome = new Object[config.getChromoLength()];
                this.fitness = 0.0;
            }

            protected Individual(Individual individual) {
                this.fitness = individual.getFitness();
                this.chromosome = individual.getChromosome();
            }

            protected Individual(double fitness) {
                this.fitness = fitness;
            }

            public Object [] getChromosome() {
                return this.chromosome;
            }

            protected void generate() {
                for(int i=0;i<chromosome.length;i++)
                    chromosome[i] = config.getAlphabet()[config.selectFromAlphabet()];
            }

            public String toString() {
                StringBuilder s = new StringBuilder();
                for(int i=0;i<this.chromosome.length;i++) {
                    s.append(this.chromosome[i].toString()+ " ");
                }
                return s.toString();
            }

            /* Crossover */
            private int getCrossOverPoint() {
                double num = Config.getRandomNumber();
                int i = 1; boolean found = false;
                while(!found && i<chromosome.length-1) {
                    if(num < i/chromosome.length-1) found = true;
                    else i++;
                }
                return i;
            }

            protected void crossOver(Individual individual) {
                int point = getCrossOverPoint();
                Object aux;
                for(int i=point; i<chromosome.length;i++) {
                    aux = this.chromosome[i];
                    this.chromosome[i] = individual.chromosome[i];
                    individual.chromosome[i] = aux;
                }
            }

            /* Mutation */
            protected void mutation() {
                for(int i=0;i<this.chromosome.length;i++) {
                    if(Config.getRandomNumber() < config.getpMutation()) this.chromosome[i] = config.getAlphabet()[config.selectFromAlphabet()];
                }
            }

            /* Fitness */
            protected void fitness() {
                this.fitness = Algorithm.this.fitness(this.chromosome); // Assign individual fitness according to custom method
            }

            protected double getFitness() {
                return this.fitness;
            }

        }
    }

}