package no.oyvegard.GA;

import java.util.List;

public interface GAIndividual {
    public void setRank(int rank);

    public int getRank();

    public void setCrowdingDistance(float distance);

    public Float getCrowdingDistance();

    public void mutate();

    public List<GAIndividual> getDomintedSolutions();

    public void addDomintedSolution(GAIndividual other);

    public void clearDominatedSolutions();

    public void setFitnessValues(List<Float> fitnessValues);

    public List<Float> getFitnessValues();

    public List<GAIndividual> crossover(GAIndividual other);

    public void calculateClusters();

}
