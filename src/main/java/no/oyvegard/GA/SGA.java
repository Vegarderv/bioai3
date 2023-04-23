package no.oyvegard.GA;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class SGA extends GA {

    private double SFACTOR = 1.9;

    private float mutationRate;
    private float crossoverRate;
    private List<GAIndividual> offspring;
    private List<Double> ranking = new ArrayList<>();

    private HashMap<Function<GAIndividual, Double>, Double> evaluationFuctions;

    public SGA(float mutationRate, float crossoverRate,
            HashMap<Function<GAIndividual, Double>, Double> evaluationFuctions) {
        this.mutationRate = mutationRate;
        this.crossoverRate = crossoverRate;
        this.evaluationFuctions = evaluationFuctions;
        createRanking();
    }

    private void createRanking() {
        for (int i = 0; i < population.size(); i++) {
            ranking.add((2 - SFACTOR) / populationSize + 2 * i * (SFACTOR - 1)
                    / (populationSize * (populationSize - 1)));
        }
    }

    @Override
    protected void evaluatePopulation() {
        population.sort((ind1, ind2) -> Double.compare(getEval(ind1), getEval(ind2)));
    }

    private double getEval(GAIndividual individual) {
        return evaluationFuctions.keySet()
                .stream()
                .map(func -> func.apply(individual) * evaluationFuctions.get(func))
                .reduce(0.0, Double::sum);
    }

    @Override
    protected void generateNewPopulation() {
        offspring.clear();
        // Create the new offspring
        for (int i = 0; i < populationSize; i++) {
            GAIndividual parent1 = chooseParent();
            GAIndividual parent2 = chooseParent();

            List<GAIndividual> children = crossover(parent1, parent2);
            mutate(children.get(0));
            mutate(children.get(1));
            offspring.add(children.get(0));
            offspring.add(children.get(1));
        }

        // merge the population and offspring
        population.addAll(offspring);

        // New evaluation
        evaluatePopulation();

    }

    private GAIndividual chooseParent() {
        double cumulativeChance = 0;
        double winner = new Random().nextDouble();
        for (int j = 0; j < population.size(); j++) {
            if (cumulativeChance + ranking.get(j) > winner) {
                return population.get(j);
            }
            cumulativeChance += ranking.get(j);
        }
        throw new IllegalStateException("No parent chosen");
    }

    private void mutate(GAIndividual individual) {
        if ((new Random()).nextFloat() < mutationRate) {
            individual.mutate(mutationRate);
        }
    }

    private List<GAIndividual> crossover(GAIndividual parent1, GAIndividual parent2) {
        List<GAIndividual> offspring = new ArrayList<>();
        if ((new Random()).nextFloat() < crossoverRate) {
            List<GAIndividual> children = parent1.crossover(parent2);
            offspring.add(children.get(0));
            offspring.add(children.get(1));
        } else {
            offspring.add(parent1);
            offspring.add(parent2);
        }

        return offspring;
    }

    @Override
    public GAIndividual getBestIndividual() {
        return population.get(populationSize - 1);
    }

    @Override
    public List<GAIndividual> getBestIndividuals(int nbr) {
        // TODO Auto-generated method stub
        return population.subList(populationSize - nbr, populationSize);
    }

}
