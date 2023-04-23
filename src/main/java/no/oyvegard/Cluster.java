package no.oyvegard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Cluster {

    private List<Piksel> pixels = new ArrayList<>();

    public void addPixel(Piksel pixel) {
        pixels.add(pixel);
    }

    public void addAllPixels(Cluster other) {
        this.pixels.addAll(other.getPixels());
    }

    public List<Piksel> getPixels() {
        return pixels;
    }

    public Piksel getCentroid() {
        if (pixels.size() == 0) {
            return null;
        }
        List<Integer> colors = pixels.stream().map(p -> {
            return Arrays.asList(p.getRed(), p.getBlue(), p.getGreen());
        }).reduce(Arrays.asList(0, 0, 0), (a, b) -> {
            return Arrays.asList(a.get(0) + b.get(0), a.get(1) + b.get(1), a.get(2) + b.get(2));
        }).stream().map(c -> c / pixels.size()).collect(Collectors.toList());

        Piksel pix = new Piksel(0, 0);
        pix.setRGB(colors.get(0), colors.get(1), colors.get(2));

        return pix;
    }

}
