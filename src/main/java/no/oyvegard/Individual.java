package no.oyvegard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import no.oyvegard.GA.*;;

public class Individual implements GAIndividual {

    private List<List<Piksel>> pixels;
    private int width;
    private int height;

    private List<List<Piksel>> clusters;

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
                row.add(new Piksel(i, j));
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

    public void calculateClusters() {
        pixels.stream().flatMap(Collection::stream).forEach(pixel -> pixel.setClusterIndex(-1));
        List<List<Piksel>> clusters = new ArrayList<>();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Piksel pixel = pixels.get(i).get(j);
                if (pixel.getClusterIndex() != -1) {
                    continue;
                }

                List<Piksel> cluster = new ArrayList<>();
                int clusterIndex = clusters.size();

                pixel.setClusterIndex(clusterIndex);

                Direction direction = pixel.getDirection();

                boolean foundOldCluster = false;

                while (direction != null) {
                    cluster.add(pixel);
                    switch (direction) {
                        case SELF:
                            break;
                        case LEFT:
                            pixel = pixels.get(pixel.getX()).get(pixel.getY() - 1);
                            break;
                        case RIGHT:
                            pixel = pixels.get(pixel.getX()).get(pixel.getY() + 1);
                            break;
                        case DOWN:
                            pixel = pixels.get(pixel.getX() + 1).get(pixel.getY());
                            break;
                        case UP:
                            pixel = pixels.get(pixel.getX() - 1).get(pixel.getY());
                            break;
                        default:
                            break;
                    }

                    if (pixel.getClusterIndex() != -1) {
                        int oldClusterIndex = pixel.getClusterIndex();
                        for (Piksel p : cluster) {
                            p.setClusterIndex(oldClusterIndex);
                        }

                        clusters.get(oldClusterIndex).addAll(cluster);
                        break;
                    }

                    pixel.setClusterIndex(clusterIndex);
                    direction = pixel.getDirection();
                }

                if (!foundOldCluster) {

                    clusters.add(cluster);
                }

            }
        }

        this.clusters = clusters;
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
