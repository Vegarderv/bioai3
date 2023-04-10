package no.oyvegard.GA;

import javax.imageio.ImageIO;

import no.oyvegard.Prim;

import java.awt.image.BufferedImage;
import java.io.File;

public class TestNSGA {
    
    BufferedImage image;

    public TestNSGA() {
        try {
            image = ImageIO.read(new File("src/main/resources/amogus.jpg"));
            new Prim(image).generateIndividual();
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
}
