package no.oyvegard;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

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
        }

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = image.getRGB(x, y);

                if (x > 0) {
                    int neighbor = image.getRGB(x - 1, y);
                    distanceMatrix.get(y * width + x).set(y * width + (x + 1), findDistance(pixel, neighbor));
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
        List<List<Integer>> visitedPixels = new ArrayList<>();
        List<Edge> distances = new ArrayList<>();
        Individual individual = new Individual(width, height);

        // Generate random starting point
        int randomX = new Random().nextInt(width);
        int randomY = new Random().nextInt(height);

        List<Integer> currentPixel = Arrays.asList(randomX, randomY);
        distances.addAll(findNeighbours(currentPixel, visitedPixels));

        while (distances.size() > 0) {
            visitedPixels.add(new ArrayList<>(currentPixel));
            Edge newEdge = distances.stream()
                    .max(Comparator.comparingDouble(Edge::getDistance))
                    .get();
            individual.changeDirection(currentPixel, newEdge.direction);
            currentPixel = newEdge.getTo();
            distances = distances.stream().filter(edge -> !edge.getTo().equals(newEdge.getTo())).toList();
        }

        return individual;

    }

    private double findDistance(int pixel1, int pixel2) {

        int red = (pixel1 >> 16) & 0xff;
        int green = (pixel1 >> 8) & 0xff;
        int blue = pixel1 & 0xff;

        int pixel2Red = (pixel2 >> 16) & 0xff;
        int pixel2Green = (pixel2 >> 8) & 0xff;
        int pixel2Blue = pixel2 & 0xff;
        return Math.sqrt(Math.pow(red - pixel2Red, 2)
                + Math.pow(green - pixel2Green, 2) + Math.pow(blue - pixel2Blue, 2));
    }

    private double findDistance(List<Integer> pixel1, List<Integer> pixel2) {
        return distanceMatrix.get(pixel1.get(1) * width + pixel1.get(0)).get(pixel2.get(1) * width + pixel2.get(0));
    }

    private List<Edge> findNeighbours(List<Integer> pixel, List<List<Integer>> visited) {
        List<Edge> newNeighbours = new ArrayList<>();

        if (pixel.get(0) > 0) {
            List<Integer> pixelNeighbour = Arrays.asList(pixel.get(0) - 1, pixel.get(1));
            if (!visited.stream().anyMatch(pix -> pix.equals(pixelNeighbour))) {
                newNeighbours.add(new Edge(pixel, pixelNeighbour, findDistance(pixel, pixelNeighbour)));
            }
        }

        if (pixel.get(0) < width - 1) {
            List<Integer> pixelNeighbour = Arrays.asList(pixel.get(0) + 1, pixel.get(1));
            if (!visited.stream().anyMatch(pix -> pix.equals(pixelNeighbour))) {
                newNeighbours.add(new Edge(pixel, pixelNeighbour, findDistance(pixel, pixelNeighbour)));
            }
        }

        if (pixel.get(1) > 0) {
            List<Integer> pixelNeighbour = Arrays.asList(pixel.get(0), pixel.get(1) - 1);
            if (!visited.stream().anyMatch(pix -> pix.equals(pixelNeighbour))) {
                newNeighbours.add(new Edge(pixel, pixelNeighbour, findDistance(pixel, pixelNeighbour)));
            }
        }

        if (pixel.get(1) < height - 1) {
            List<Integer> pixelNeighbour = Arrays.asList(pixel.get(0), pixel.get(1) + 1);
            if (!visited.stream().anyMatch(pix -> pix.equals(pixelNeighbour))) {
                newNeighbours.add(new Edge(pixel, pixelNeighbour, findDistance(pixel, pixelNeighbour)));
            }
        }

        return newNeighbours;
    }
}
