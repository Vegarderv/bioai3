package no.oyvegard.GA;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class NSGA extends GA {

    private float mutationRate;
    private float crossoverRate;
    private List<Function<GAIndividual, Double>> fitnessFunctions;

    private List<GAIndividual> offspring = new ArrayList<>();
    private List<List<GAIndividual>> frontList = new ArrayList<>();

    public NSGA(float mutationRate, float crossoverRate, int populationSize,
            List<Function<GAIndividual, Double>> fitnessFunctions) {
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
        population.parallelStream().forEach(
                individual -> {
                    individual.calculateClusters();
                    individual.setFitnessValues(
                            fitnessFunctions.stream().map(f -> -f.apply(individual)).collect(Collectors.toList()));
                });
    }

    @Override
    protected void generateNewPopulation() {
        // Create the new offspring
        offspring = IntStream.range(0, populationSize).parallel().mapToObj(i -> {
            GAIndividual parent1 = tournamentParentSelection();
            GAIndividual parent2 = tournamentParentSelection();

            List<GAIndividual> children = crossover(parent1, parent2);
            mutate(children.get(0));
            mutate(children.get(1));
            return children;
        }).flatMap(List::stream).collect(Collectors.toList());

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

                        if (!nextFront.contains(dominated)) {
                            nextFront.add(dominated);
                        }
                    }
                }
            }

            i += 1;
            if (nextFront.size() > 0) {
                frontList.add(new ArrayList<>(nextFront));
            }
            currentFront = new ArrayList<>(nextFront);

        }

    }

    private boolean dominates(GAIndividual individual, GAIndividual other) {
        int strictCount = 0;
        List<Double> f1 = individual.getFitnessValues();
        List<Double> f2 = other.getFitnessValues();
        for (int i = 0; i < f1.size(); i++) {
            if (f1.get(i) > f2.get(i)) {
                return false;
            }
            if (f1.get(i) < f2.get(i)) {
                strictCount += 1;
            }
        }

        return strictCount > 0;

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
                double m1 = (front.get(j - 1).getFitnessValues().get(i));
                double m2 = (front.get(j + 1).getFitnessValues().get(i));

                double distance = front.get(j).getCrowdingDistance() + (m2 - m1) / (fmax - fmin);
                front.get(j).setCrowdingDistance(distance);

            }
        }
    }

    private void limitSortedPopulation() {
        int targetSize = populationSize;
        int size = 0;

        List<GAIndividual> newPopulation = new ArrayList<>();

        for (List<GAIndividual> front : frontList) {
            if (size + front.size() < targetSize) {
                newPopulation.addAll(front);
            } else {
                front.sort((GAIndividual a, GAIndividual b) -> b.getCrowdingDistance()
                        .compareTo(a.getCrowdingDistance()));
                newPopulation.addAll(front.subList(0, targetSize - size));
                break;
            }
        }

        population = newPopulation;

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
            offspring.add(parent1.clone());
            offspring.add(parent2.clone());
        }

        return offspring;
    }

    @Override
    public GAIndividual getBestIndividual() {
        evaluatePopulation();
        return frontList.get(0).get(0);
    }

    @Override
    public List<GAIndividual> getBestIndividuals(int nbr) {
        evaluatePopulation();

        int l = frontList.get(0).size();
        if (l < nbr) {
            return frontList.get(0);
        }

        float spread = ((float) l / (float) (nbr + 2));
        List<GAIndividual> result = new ArrayList<>();
        System.out.println(frontList.get(0).size());
        for (int i = 1; i < nbr + 1; i++) {
            result.add(frontList.get(0).get(Math.round(spread * i)));
        }

        return result;
    }

}