package opt.ga;

import java.util.Random;
import java.util.Comparator;
import java.util.Arrays;

import dist.DiscreteDistribution;

import opt.OptimizationAlgorithm;
import shared.Instance;
import util.RandomProvider;


/**
 * Genetic algorithms are pretty stupid.
 * This is based on the version in Andrew Moore's tutorial.
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class StandardGeneticAlgorithm extends OptimizationAlgorithm {
    
    /**
     * The random number generator
     */
    private static final Random random = RandomProvider.get();
    
    /**
     * The population size
     */
    private int populationSize;
    
    /**
     * The number of population to mate
     * each time step
     */
    private int toMate;
    
    /**
     * The number of population to mutate
     * each time step
     */
    private int toMutate;
    
    class Citizen
    {
        Instance data;
        double value;
    };

    /**
     * The population
     */
    private Citizen [] population;
    
    /**
     * Make a new genetic algorithm
     * @param populationSize the size
     * @param toMate the number to mate each iteration
     * @param toMutate the number to mutate each iteration
     * @param gap the problem to solve
     */
    public StandardGeneticAlgorithm(int populationSize, int toMate, int toMutate, GeneticAlgorithmProblem gap) {
        super(gap);
        this.toMate = toMate;
        this.toMutate = toMutate;
        this.populationSize = populationSize;
        population = new Citizen[populationSize];
        for (int i = 0; i < population.length; i++) {
            population[i] = new Citizen();
            population[i].data = gap.random();
            population[i].value = gap.value(population[i].data);
        }
    }

    /**
     * @see shared.Trainer#train()
     */
    public double train() {
        GeneticAlgorithmProblem ga = (GeneticAlgorithmProblem) getOptimizationProblem();
        double[] probabilities = new double[population.length];
        // calculate probability distribution over the population
        double sum = 0;
        for (int i = 0; i < probabilities.length; i++) {
            probabilities[i] = population[i].value;
            sum += probabilities[i];
        }
        if (Double.isInfinite(sum)) {
            return sum;
        }
        for (int i = 0; i < probabilities.length; i++) {
            probabilities[i] /= sum;
        }
        DiscreteDistribution dd = new DiscreteDistribution(probabilities);
  
        // make the children
        Citizen[] newPopulation = new Citizen[populationSize];
        
        int insertAt = 0;
        for (int i = 0; i < toMate; i++) {
            // pick the mates
            Instance a = population[dd.sample(null).getDiscrete()].data;
            Instance b = population[dd.sample(null).getDiscrete()].data;
            // make the kid
            newPopulation[insertAt] = new Citizen();
            newPopulation[insertAt].data = ga.mate(a, b);
            insertAt++;
        }
        // mutate
        for (int i = 0; i < toMutate; i++) {
            int j = random.nextInt(newPopulation.length);
            newPopulation[insertAt] = new Citizen();
            newPopulation[insertAt].data = (Instance) population[j].data.copy();
            ga.mutate(newPopulation[insertAt].data);
            insertAt++;
        }
        
        // Now sort population by value
        sort();
        System.out.println(population[0].value);
        
        // elite for the rest
        for (int i = 0; i < newPopulation.length - toMate - toMutate; i++) {
            newPopulation[insertAt] = new Citizen();
            newPopulation[insertAt].data = (Instance) population[i].data.copy();
            newPopulation[insertAt].value = population[i].value;
            insertAt++;
        }
        // calculate the new values
        for (int i = 0; i < toMate + toMutate; i++) {
            newPopulation[i].value = ga.value(newPopulation[i].data);
        }
        // the new generation
        population = newPopulation;
        return sum / populationSize;
    }

    /**
     * @see opt.OptimizationAlgorithm#getOptimalData()
     */
    public Instance getOptimal() {
        sort();
        return population[0].data;
    }
    
    private void sort()
    {
        Arrays.sort(population, new Comparator<Citizen>() {
          @Override
          public int compare(Citizen o1, Citizen o2) {
            // Desending.
            return Double.compare(o2.value, o1.value);
          }
        });
    }

}
