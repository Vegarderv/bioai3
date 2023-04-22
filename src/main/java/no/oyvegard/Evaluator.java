package no.oyvegard;

import java.util.Collection;
import java.util.List;

import no.oyvegard.GA.GAIndividual;

class Evaluator {

    private static double euclideanPixelDistance(Piksel a, Piksel b) {
        return Math.sqrt(Math.pow(a.getRed() - b.getRed(), 2) + Math.pow(a.getGreen() - b.getGreen(), 2)
                + Math.pow(a.getBlue() - b.getBlue(), 2));
    }

    public static double EdgeValue(GAIndividual individual) {
        List<Integer> imageSize = individual.getSize();
        int width = imageSize.get(0);
        int height = imageSize.get(1);

        double value = individual.getPixels().stream().flatMap(Collection::stream).mapToDouble(pixel -> {
            return pixel.getNeighbourIndices(width, height).stream().mapToDouble(index -> {
                Piksel neighbour = individual.getPixels().get(index.get(1)).get(index.get(0));
                return neighbour.getClusterIndex() == pixel.getClusterIndex() ? 0.0
                        : Evaluator.euclideanPixelDistance(pixel, neighbour);
            }).sum();
        }).sum();

        System.out.println("EdgeValue: " + value);

        return value;

    }

}
