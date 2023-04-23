package no.oyvegard.GA;

import javax.imageio.ImageIO;

import no.oyvegard.Direction;
import no.oyvegard.Image;
import no.oyvegard.Individual;
import no.oyvegard.Prim;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Collection;

public class TestNSGA {

    BufferedImage image;

    public TestNSGA() {
        try {
            image = ImageIO.read(new File("src/main/resources/amogus.png"));
        } catch (Exception e) {
            // TODO: handle exception
        }
        Prim prim = new Prim(image);
        Individual solution = prim.generateIndividual();
        solution.calculateClusters();
        Image img = new Image(image, solution);
        img.drawSegmentedImage(2, "test.jpg");
    }

    public static void main(String[] args) {
        new TestNSGA();
    }
}
