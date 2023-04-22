package no.oyvegard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import no.oyvegard.GA.*;

public class Individual implements GAIndividual {

    private List<List<Piksel>> pixels;
    private int width;
    private int height;

    private List<Cluster> clusters = new ArrayList<>();

    private int rank;
    private double crowdingDistance;
    private List<GAIndividual> dominatedSolutions = new ArrayList<>();
    private List<Double> fitnessValues = new ArrayList<>();

    public Individual(int width, int height) {
        this.width = width;
        this.height = height;
        generateBoard();
    }

    public Individual(Individual ind, List<List<Piksel>> pixels) {
        this.height = ind.getHeight();
        this.width = ind.getWidth();
        this.pixels = pixels;
    }

    private void generateBoard() {
        List<List<Piksel>> board = new ArrayList<>();
        for (int i = 0; i < height; i++) {
            List<Piksel> row = new ArrayList<>();
            for (int j = 0; j < width; j++) {
                row.add(new Piksel(j, i));
            }
            board.add(row);
        }
        this.pixels = board;
    }

    public List<List<Piksel>> getPixels() {
        return pixels;
    }

    private List<List<Piksel>> unFlatMapper(List<Piksel> pixles) {
        List<List<Piksel>> outList = new ArrayList<>();
        for (int i = 0; i < height; i++) {
            List<Piksel> row = new ArrayList<>();
            for (int j = 0; j < width; j++) {
                row.add(pixles.get(i * width + j));
            }
            outList.add(row);
        }
        return outList;
    }

    public void changeDirection(List<Integer> pixel, Direction direction) {
        pixels.get(pixel.get(1)).get(pixel.get(0)).setDirection(direction);
    }

    public void changePixelColor(int x, int y, int r, int g, int b) {
        Piksel pixel = pixels.get(y).get(x);
        pixel.setRGB(r, g, b);
    }

    public void calculateClusters() {
        pixels.stream().flatMap(Collection::stream).forEach(pixel -> pixel.setClusterIndex(-1));
        List<Cluster> clusters = new ArrayList<>();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Piksel pixel = pixels.get(i).get(j);
                if (pixel.getClusterIndex() != -1) {
                    continue;
                }

                Cluster cluster = new Cluster();
                int clusterIndex = clusters.size();

                pixel.setClusterIndex(clusterIndex);

                Direction direction = pixel.getDirection();

                boolean foundOldCluster = false;

                while (direction != null) {
                    cluster.addPixel(pixel);
                    switch (direction) {
                        case SELF:
                            break;
                        case LEFT:
                            pixel = pixels.get(pixel.getY()).get(pixel.getX() - 1);
                            break;
                        case RIGHT:
                            pixel = pixels.get(pixel.getY()).get(pixel.getX() + 1);
                            break;
                        case UP:
                            pixel = pixels.get(pixel.getY() - 1).get(pixel.getX());
                            break;
                        case DOWN:
                            pixel = pixels.get(pixel.getY() + 1).get(pixel.getX());
                            break;
                        default:
                            break;
                    }

                    if (pixel.getClusterIndex() != -1) {
                        int oldClusterIndex = pixel.getClusterIndex();
                        for (Piksel p : cluster.getPixels()) {
                            p.setClusterIndex(oldClusterIndex);
                        }

                        clusters.get(oldClusterIndex).addAllPixels(cluster);
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
    public void setCrowdingDistance(double distance) {
        this.crowdingDistance = distance;
    }

    @Override
    public Double getCrowdingDistance() {
        return crowdingDistance;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public void mutate(double mutationRate) {
        pixels.stream().flatMap(Collection::stream).filter(pix -> new Random().nextDouble() < mutationRate).forEach(pix -> pix.mutate(this.width, this.height));
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
    public void setFitnessValues(List<Double> fitnessValues) {
        this.fitnessValues = fitnessValues;
    }

    @Override
    public List<Double> getFitnessValues() {
        return fitnessValues;
    }

    @Override
    public List<GAIndividual> crossover(GAIndividual other) {
        int splitPoint = new Random().nextInt(width * height - 3) + 1;
        List<GAIndividual> res = new ArrayList<>();
        List<Piksel> pixels1 = this.getPixels().stream().flatMap(Collection::stream).toList();
        List<Piksel> pixels2 = other.getPixels().stream().flatMap(Collection::stream).toList();

        List<Piksel> newPixels1 = pixels1.subList(0, splitPoint);
        pixels1.addAll(pixels2.subList(splitPoint, width * height - 1));
        
        List<Piksel> newPixels2 = pixels2.subList(0, splitPoint);
        pixels2.addAll(pixels1.subList(splitPoint, width * height - 1));

        res.add(new Individual(this, unFlatMapper(newPixels1)));
        res.add(new Individual(this, unFlatMapper(newPixels2)));

        return res;
    }

    @Override
    public List<Integer> getSize() {
        int height = pixels.size();
        int width = pixels.get(0).size();

        return Arrays.asList(width, height);
    }

    public void printDirections() {
        // Prints a grid of the directions of the pixels
        for (List<Piksel> row : pixels) {
            for (Piksel pixel : row) {

                Direction dir = pixel.getDirection();
                if (dir == null) {
                    System.out.print(" ");
                    continue;
                }
                System.out.print(pixel.getDirection().toString().charAt(0));
            }
            System.out.println();
        }
    }

    public List<Cluster> getClusters() {
        return clusters;
    }

    public void setClusters(List<Cluster> clusters) {
        this.clusters = clusters;
    }

}
