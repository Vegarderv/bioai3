package no.oyvegard;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
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
        for (List<Piksel> cluster : individual.getClusters()) {
            List<Piksel> pixelsInCluster = individual.getPixels().stream().flatMap(Collection::stream).filter(pix -> pix.getClusterIndex() == cluster.get(0).getClusterIndex()).toList();
            for (Piksel piksel : cluster) {
                List<List<Integer>> neighbours = piksel.getNeighbourIndices(this.width, this.height);
                List<Piksel> adjPixelsInCluster = pixelsInCluster.stream().filter(pix -> neighbours.contains(Arrays.asList(pix.getX(), pix.getY()))).toList();
                if (adjPixelsInCluster.size() < 4) {
                    piksel.setIsBorder(true);
                }
            }
        }
    }

    public void drawSegmentedImage(int segmentationType) {
        BufferedImage image;
        Color color;
        if (segmentationType == 1) {
            image = this.img;
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
        this.individual.getPixels().stream().flatMap(Collection::stream).filter(pix -> pix.getIsBorder()).forEach(pix -> image.setRGB(pix.getX(), pix.getY(), color.getRGB()));

        try {
            ImageIO.write(image, "PNG", new File("test_image.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
