package no.oyvegard;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import javax.imageio.ImageIO;

import no.oyvegard.GA.NSGA;
import no.oyvegard.GA.*;

public class Main {

	public static void main(String[] args) {
		List<Function<GAIndividual, Double>> fitnessFunctions = new ArrayList<>();
		fitnessFunctions.add((individual) -> Evaluator.EdgeValue(individual));
		fitnessFunctions.add((individual) -> Evaluator.ConnectivityMeasure(individual));

		NSGA ga = new NSGA(0.2f, 0.8f, 100, fitnessFunctions);
		try {
			BufferedImage image = ImageIO.read(new File("src/main/resources/amogus.png"));

			ga.run(image, 10);

			System.out.println("Done");

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("ERROR");
			e.printStackTrace();
		}
	}
}