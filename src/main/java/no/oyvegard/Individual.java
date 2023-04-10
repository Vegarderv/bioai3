package no.oyvegard;

import java.util.ArrayList;
import java.util.List;
import no.oyvegard.GA.*;;

public class Individual implements GAIndividual {

    private List<List<Piksel>> pixels;
    private int width;
    private int height;

    private int rank;
    private float crowdingDistance;
    private List<GAIndividual> dominatedSolutions = new ArrayList<>();
    private List<Float> fitnessValues = new ArrayList<>();

    public Individual(int width, int height) {
        this.width = width;
        this.height = height;
        generateBoard();
    }

    private void generateBoard() {
        List<List<Piksel>> board = new ArrayList<>();
        for (int i = 0; i < height; i++) {
            List<Piksel> row = new ArrayList<>();
            for (int j = 0; j < width; j++) {
                row.add(new Piksel());
            }
            board.add(row);
        }
        this.pixels = board;
    }

    public List<List<Piksel>> getPixels() {
        return pixels;
    }

    public void changeDirection(List<Integer> pixel, Direction direction) {
        pixels.get(pixel.get(1)).get(pixel.get(0)).setDirection(direction);
    }

    @Override
    public void setRank(int rank) {
        this.rank = rank;
    }

    @Override
    public int getRank() {
        return rank;
    }

    @Override
    public void setCrowdingDistance(float distance) {
        this.crowdingDistance = distance;
    }

    @Override
    public Float getCrowdingDistance() {
        return crowdingDistance;
    }

    @Override
    public void mutate() {
        // TODO Auto-generated method stub
    }

    @Override
    public List<GAIndividual> getDomintedSolutions() {
        return dominatedSolutions;
    }

    @Override
    public void addDomintedSolution(GAIndividual other) {
        this.dominatedSolutions.add(other);
    }

    @Override
    public void clearDominatedSolutions() {
        this.dominatedSolutions.clear();
    }

    @Override
    public void setFitnessValues(List<Float> fitnessValues) {
        this.fitnessValues = fitnessValues;
    }

    @Override
    public List<Float> getFitnessValues() {
        return fitnessValues;
    }

    @Override
    public List<GAIndividual> crossover(GAIndividual other) {
        List<GAIndividual> res = new ArrayList<>();
        res.add(this);
        res.add(other);

        return res;
    }

}
