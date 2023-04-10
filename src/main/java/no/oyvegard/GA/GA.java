package no.oyvegard.GA;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import no.oyvegard.Individual;
import no.oyvegard.Prim;

public abstract class GA {

    private BufferedImage image;

    protected int populationSize;
    protected List<GAIndividual> population = new ArrayList<>();

    public void run(BufferedImage image, int nbrGenerations) {
        this.image = image;
        initPopulation();
        System.out.println("Population initialized" + population.size());
        for (int i = 0; i < nbrGenerations; i++) {
            runGeneration();

        }
    }

    protected void initPopulation() {
        Prim prim = new Prim(image);

        for (int i = 0; i < populationSize; i++) {
            Individual individual = prim.generateIndividual();
            population.add(individual);

        }

    }

    private void runGeneration() {
        evaluatePopulation();
        logPerformance();
        generateNewPopulation();

    }

    private void logPerformance() {
    }

    protected abstract void evaluatePopulation();

    protected abstract void generateNewPopulation();

}
