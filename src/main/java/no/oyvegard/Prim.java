package no.oyvegard;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Prim {

    public BufferedImage image;
    public int height;
    public int width;
    public List<List<Double>> distanceMatrix;

    public Prim(BufferedImage image) {
        this.image = image;
        this.height = image.getHeight();
        this.width = image.getWidth();
        generateDistanceMatrix();

    }

    private void generateDistanceMatrix() {
        List<List<Double>> distanceMatrix = new ArrayList<>();

        // Generating an empty matrix
        for (int y = 0; y < height * width; y++) {
            List<Double> row = new ArrayList<>();
            for (int x = 0; x < height * width; x++) {
                row.add(0.0);
            }
            distanceMatrix.add(row);
        }

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = image.getRGB(x, y);

                if (x > 0) {
                    int neighbor = image.getRGB(x - 1, y);
                    distanceMatrix.get(y * width + x).set(y * width + (x - 1), findDistance(pixel, neighbor));
                }

                if (x < width - 1) {
                    int neighbor = image.getRGB(x + 1, y);
                    distanceMatrix.get(y * width + x + 1).set(y * width + (x + 1), findDistance(pixel, neighbor));
                }
                if (y > 0) {
                    int neighbor = image.getRGB(x, y - 1);
                    distanceMatrix.get(y * width + x).set((y - 1) * width + x, findDistance(pixel, neighbor));
                }
                if (y < height - 1) {
                    int neighbor = image.getRGB(x, y + 1);
                    distanceMatrix.get(y * width + x).set((y + 1) * width + x, findDistance(pixel, neighbor));
                }
            }
        }

        this.distanceMatrix = distanceMatrix;

    }

    public Individual generateIndividual() {
        List<Edge> distances = new ArrayList<>();
        Individual individual = new Individual(width, height, image);

        // Generate random starting point
        int randomX = new Random().nextInt(width);
        int randomY = new Random().nextInt(height);

        Piksel currentPixel = individual.getPixels().get(randomY).get(randomX);
        distances.addAll(findNeighbours(currentPixel, individual));

        while (distances.size() > 0) {
            currentPixel.setVisited(true);
            Edge newEdge = distances
                    .stream()
                    .min(Comparator.comparingDouble(Edge::getDistance))
                    .get();
            currentPixel = newEdge.getTo();
            distances = distances
                    .stream()
                    .filter(edge -> !edge.getTo().equals(newEdge.getTo()))
                    .collect(Collectors.toList());
            distances.addAll(findNeighbours(currentPixel, individual));
        }

        return individual;

    }

    private List<Integer> getRGB(int pixel1) {
        int red = (pixel1 >> 16) & 0xff;
        int green = (pixel1 >> 8) & 0xff;
        int blue = pixel1 & 0xff;

        return Arrays.asList(red, green, blue);

    }

    private double findDistance(int pixel1, int pixel2) {
        List<Integer> rgb = getRGB(pixel1);
        int red = rgb.get(0);
        int green = rgb.get(1);
        int blue = rgb.get(2);

        int pixel2Red = (pixel2 >> 16) & 0xff;
        int pixel2Green = (pixel2 >> 8) & 0xff;
        int pixel2Blue = pixel2 & 0xff;
        return Math.sqrt(Math.pow(red - pixel2Red, 2)
                + Math.pow(green - pixel2Green, 2) + Math.pow(blue - pixel2Blue, 2));
    }

    private List<Edge> findNeighbours(Piksel pixel, Individual individual) {
        List<Edge> newNeighbours = new ArrayList<>();


        if (pixel.getX() > 0) {
            Piksel pixelNeighbour = individual.getPixels().get(pixel.getY()).get(pixel.getX() - 1);
            if (!pixelNeighbour.isVisited()) {
                newNeighbours.add(new Edge(pixel, pixelNeighbour, findDistance(pixel.getRGB(), pixelNeighbour.getRGB())));
            }
        }
        if (pixel.getX() < width - 1) {
            Piksel pixelNeighbour = individual.getPixels().get(pixel.getY()).get(pixel.getX() + 1);
            if (!pixelNeighbour.isVisited()) {
                newNeighbours.add(new Edge(pixel, pixelNeighbour, findDistance(pixel.getRGB(), pixelNeighbour.getRGB())));
            }
        }
        if (pixel.getY() < height - 1) {
            Piksel pixelNeighbour = individual.getPixels().get(pixel.getY() + 1).get(pixel.getX());
            if (!pixelNeighbour.isVisited()) {
                newNeighbours.add(new Edge(pixel, pixelNeighbour, findDistance(pixel.getRGB(), pixelNeighbour.getRGB())));
            }
        }
        if (pixel.getY() > 0) {
            Piksel pixelNeighbour = individual.getPixels().get(pixel.getY() - 1).get(pixel.getX());
            if (!pixelNeighbour.isVisited()) {
                newNeighbours.add(new Edge(pixel, pixelNeighbour, findDistance(pixel.getRGB(), pixelNeighbour.getRGB())));
            }
        }

        return newNeighbours;
    }
}
