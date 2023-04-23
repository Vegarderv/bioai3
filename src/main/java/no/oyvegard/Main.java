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
		fitnessFunctions.add((individual) -> Evaluator.OverallDeviation(individual));

		RunConfig config = new RunConfig();

		NSGA ga = new NSGA(config.mutationRate, config.crossoverRate, config.populationSize, fitnessFunctions);
		try {
			BufferedImage image = ImageIO.read(new File("training_images/86016/Test image.jpg"));

			ga.run(image, config.nbrGenerations);

			System.out.println("Done");

			Individual best = (Individual) ga.getBestIndividual();

			Image segmented = new Image(image, best);
			segmented.outputSegmentedImages(config.outputPath);

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("ERROR");
			e.printStackTrace();
		}
	}
}