package no.oyvegard.GA;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import me.tongfei.progressbar.ProgressBar;
import no.oyvegard.Individual;
import no.oyvegard.Prim;

public abstract class GA {

    private BufferedImage image;

    protected int populationSize;
    protected List<GAIndividual> population = new ArrayList<>();

    public void run(BufferedImage image, int nbrGenerations) {
        this.image = image;
        initPopulation();
        System.out.println("Populationzed" + population.size());
        List<Integer> range = IntStream.range(0, nbrGenerations).boxed().collect(Collectors.toList());
        for (int i : ProgressBar.wrap(new ArrayList<>(range), "Running GA")) {
            runGeneration();
        }
    }

    protected void initPopulation() {

        population = IntStream.range(0, populationSize).parallel().mapToObj(i -> {
            Prim prim = new Prim(image);
            return prim.generateIndividual();
        })
                .collect(Collectors.toList());

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

    public abstract GAIndividual getBestIndividual();

    public abstract List<GAIndividual> getBestIndividuals(int nbr);

}
