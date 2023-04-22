package no.oyvegard.GA;

import java.util.List;

import no.oyvegard.Piksel;

public interface GAIndividual {
    public void setRank(int rank);

    public int getRank();

    public void setCrowdingDistance(double distance);

    public Double getCrowdingDistance();

    public void mutate();

    public List<GAIndividual> getDomintedSolutions();

    public void addDomintedSolution(GAIndividual other);

    public void clearDominatedSolutions();

    public void setFitnessValues(List<Double> fitnessValues);

    public List<Double> getFitnessValues();

    public List<GAIndividual> crossover(GAIndividual other);

    public void calculateClusters();

    public List<List<Piksel>> getPixels();

    public List<Integer> getSize();

}
