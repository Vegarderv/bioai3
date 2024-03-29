package no.oyvegard.GA;

import java.util.List;

import no.oyvegard.*;

public interface GAIndividual {
    public void setRank(int rank);

    public int getRank();

    public void setCrowdingDistance(double distance);

    public Double getCrowdingDistance();

    public void mutate(double mutationRate);

    public List<GAIndividual> getDomintedSolutions();

    public void addDomintedSolution(GAIndividual other);

    public void clearDominatedSolutions();

    public void setFitnessValues(List<Double> fitnessValues);

    public List<Double> getFitnessValues();

    public List<GAIndividual> crossover(GAIndividual other);

    public void calculateClusters();

    public List<Cluster> getClusters();

    public List<List<Piksel>> getPixels();

    public List<Integer> getSize();

    public GAIndividual clone();

}
