package no.oyvegard.GA;

import javax.imageio.ImageIO;

import no.oyvegard.Prim;

import java.awt.image.BufferedImage;
import java.io.File;

public class TestNSGA {
    
    BufferedImage image;

    public TestNSGA() {
        try {
            image = ImageIO.read(new File("src/main/resources/amogus.png"));
        } catch (Exception e) {
            // TODO: handle exception
        }
        Prim prim = new Prim(image);
        prim.generateIndividual();
    }
}
