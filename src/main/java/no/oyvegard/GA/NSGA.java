package no.oyvegard.GA;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;

public class NSGA extends GA {

    private float mutationRate;
    private float crossoverRate;
    private List<Function<GAIndividual, Float>> fitnessFunctions;

    private List<GAIndividual> offspring = new ArrayList<>();
    private List<List<GAIndividual>> frontList = new ArrayList<>();

    public NSGA(float mutationRate, float crossoverRate, int populationSize,
            List<Function<GAIndividual, Float>> fitnessFunctions) {
        this.mutationRate = mutationRate;
        this.crossoverRate = crossoverRate;
        this.populationSize = populationSize;
        this.fitnessFunctions = fitnessFunctions;

    }

    @Override
    protected void evaluatePopulation() {
        // non-domination sorting + crowding distance sorting
        calculateFitnessValues();
        nonDominationSorting();
        crowdingDistanceSortedPopulation();

    }

    private void calculateFitnessValues() {
        population.forEach(
                individual -> {
                    individual.calculateClusters();
                    individual.setFitnessValues(
                            fitnessFunctions.stream().map(f -> f.apply(individual)).collect(Collectors.toList()));
                });
    }

    @Override
    protected void generateNewPopulation() {
        // Create the new offspring
        for (int i = 0; i < populationSize; i++) {
            GAIndividual parent1 = tournamentParentSelection();
            GAIndividual parent2 = tournamentParentSelection();

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

        // select the best individuals as our result
        limitSortedPopulation();

    }

    public void nonDominationSorting() {
        List<GAIndividual> currentFront = new ArrayList<>();
        for (GAIndividual individual : population) {
            individual.setRank(0);
            individual.clearDominatedSolutions();
            for (GAIndividual other : population) {
                if (dominates(individual, other)) {
                    individual.addDomintedSolution(other);
                } else if (dominates(other, individual)) {
                    individual.setRank(individual.getRank() + 1);
                }
            }
            if (individual.getRank() == 0) {
                currentFront.add(individual);
            }
        }

        frontList = new ArrayList<>();
        frontList.add(new ArrayList<>(currentFront));
        int i = 0;
        while (currentFront.size() > 0) {
            List<GAIndividual> nextFront = new ArrayList<>();

            for (GAIndividual individual : currentFront) {
                for (GAIndividual dominated : individual.getDomintedSolutions()) {
                    dominated.setRank(dominated.getRank() - 1);
                    if (dominated.getRank() == 0) {
                        dominated.setRank(i + 1);
                        nextFront.add(dominated);
                    }
                }
            }

            i += 1;
            if (nextFront.size() > 0) {
                frontList.add(nextFront);
            }
            currentFront = new ArrayList<>(nextFront);

        }

    }

    private boolean dominates(GAIndividual individual, GAIndividual other) {
        boolean res = true;
        List<Float> f1 = individual.getFitnessValues();
        List<Float> f2 = other.getFitnessValues();
        for (int i = 0; i < f1.size(); i++) {
            res = res && (f1.get(i) <= f2.get(i));
        }

        return res;

    }

    public void crowdingDistanceSortedPopulation() {
        frontList.forEach(front -> crowdingDistanceSortInsideFront(front));
    }

    private void crowdingDistanceSortInsideFront(List<GAIndividual> front) {
        int l = front.size();
        if (l == 0) {
            return;
        }
        if (l == 1) {
            front.get(0).setCrowdingDistance(Float.MAX_VALUE);
            return;
        }
        if (l == 2) {
            front.get(0).setCrowdingDistance(Float.MAX_VALUE);
            front.get(1).setCrowdingDistance(Float.MAX_VALUE);
            return;
        }
        front.forEach(individual -> individual.setCrowdingDistance(0));
        for (int i = 0; i < fitnessFunctions.size(); i++) {
            final int x = i;
            front.sort((GAIndividual a, GAIndividual b) -> a.getFitnessValues().get(x)
                    .compareTo(b.getFitnessValues().get(x)));

            front.get(0).setCrowdingDistance(Float.MAX_VALUE);
            front.get(l - 1).setCrowdingDistance(Float.MAX_VALUE);

            float fmax = (float) front.stream().mapToDouble(individual -> individual.getFitnessValues().get(x)).max()
                    .getAsDouble();
            float fmin = (float) front.stream().mapToDouble(individual -> individual.getFitnessValues().get(x)).min()
                    .getAsDouble();

            for (int j = 2; j < l - 1; j++) {
                float m1 = (front.get(j - 1).getFitnessValues().get(i));
                float m2 = (front.get(j + 1).getFitnessValues().get(i));

                float distance = front.get(j).getCrowdingDistance() + (m2 - m1) / (fmax - fmin);
                front.get(j).setCrowdingDistance(distance);

            }
        }
    }

    private void limitSortedPopulation() {
        int targetSize = populationSize / 2;
        int size = 0;

        List<GAIndividual> newPopulation = new ArrayList<>();

        for (List<GAIndividual> front : frontList) {
            if (size + front.size() < targetSize) {
                newPopulation.addAll(front);
            } else {
                front.sort((GAIndividual a, GAIndividual b) -> a.getCrowdingDistance()
                        .compareTo(b.getCrowdingDistance()));
                newPopulation.addAll(front.subList(0, targetSize - size));
                break;
            }
        }

    }

    private GAIndividual tournamentParentSelection() {
        Random random = new Random();

        GAIndividual parent1 = population.get(random.nextInt(population.size()));
        GAIndividual parent2 = population.get(random.nextInt(population.size()));

        if (parent1.getRank() > parent2.getRank()) {
            return parent1;
        } else if (parent1.getRank() < parent2.getRank()) {
            return parent2;
        } else {
            if (parent1.getCrowdingDistance() > parent2.getCrowdingDistance()) {
                return parent1;
            } else {
                return parent2;
            }
        }

    }

    private void mutate(GAIndividual individual) {
        if ((new Random()).nextFloat() < mutationRate) {
            individual.mutate();
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

}