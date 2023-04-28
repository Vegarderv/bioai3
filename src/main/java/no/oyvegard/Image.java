package no.oyvegard;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;

public class Image {
    private BufferedImage img;
    private Individual individual;
    private int width;
    private int height;
    private HashMap<String, String> colors = new HashMap<>();

    public Image(BufferedImage img, Individual individual) {
        this.img = img;
        this.individual = individual;
        this.height = img.getHeight();
        this.width = img.getWidth();
        colors.put("COLOR", "#99ff99");
        colors.put("BLACK", "#000000");
        generateBorders();
    }

    private void generateBorders() {
        individual.getPixels().stream().flatMap(Collection::stream).forEach(pixel -> {
            List<List<Integer>> neighbours = pixel.getNeighbourIndices(this.width, this.height);
            int sameClusterCount = neighbours.stream().map(indices -> {
                return individual.getPixels().get(indices.get(1)).get(indices.get(0));
            }).filter(pix -> pix.getClusterIndex() == pixel.getClusterIndex()).toList().size();

            pixel.setIsBorder(sameClusterCount < 6 || neighbours.size() < 8);
        });
    }

    public void outputSegmentedImages(String outputPath, String fileName) {
        drawSegmentedImage(1, outputPath + "1.jpg");
        drawSegmentedImage(2, outputPath + "2.jpg");
    }

    public void drawSegmentedImage(int segmentationType, String outputPath) {
        BufferedImage image;
        Color color;
        if (segmentationType == 1) {
            image = deepCopy(this.img);
            color = Color.GREEN;
        } else {
            image = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_RGB);

            // Get the Graphics object from the BufferedImage
            Graphics graphics = image.getGraphics();

            // Set the color to white and draw a filled rectangle of the desired size
            graphics.setColor(Color.WHITE);
            graphics.fillRect(0, 0, this.width, this.height);

            // Dispose of the Graphics object to release resources
            graphics.dispose();

            color = Color.BLACK;
        }
        this.individual.getPixels()
                .stream()
                .flatMap(Collection::stream)
                .filter(pix -> pix.getIsBorder())
                .forEach(pix -> image.setRGB(pix.getX(), pix.getY(), color.getRGB()));

        try {
            ImageIO.write(image, "PNG", new File(outputPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }

}
