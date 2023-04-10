package no.oyvegard.GA;

import java.util.List;

public class NSGA {

    private float mutationRate;
    private float crossoverRate;
    private int tournamentSize;
    private float generationGap;
    private int populationSize;


    private List<GAIndividual> population;


    public NSGA(float mutationRate, float crossoverRate, int tournamentSize, float generationGap, int populationSize) {
        this.mutationRate = mutationRate;
        this.crossoverRate = crossoverRate;
        this.tournamentSize = tournamentSize;
        this.generationGap = generationGap;
        this.populationSize = populationSize;

        initPopulation();


    }

    private void initPopulation() {

    }

    public void run(int nbrGenerations) {
        for (int i = 0; i < nbrGenerations; i++) {
            runGeneration();
        }
    }

    private void runGeneration() {
        evaluatePopulation();
        logPerformance();
        generateOffspring();
        survivorSelection();

    }

    private void logPerformance() {

    }

    private void evaluatePopulation() {

    }

    private void generateOffspring() {

    }

    private void survivorSelection() {

    }


}