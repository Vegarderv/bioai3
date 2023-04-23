package no.oyvegard;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.function.Function;

import javax.imageio.ImageIO;

import no.oyvegard.GA.*;

public class MainSGA {
    public static void main(String[] args) {
		HashMap<Function<GAIndividual, Double>, Double> fitnessFunctions = new HashMap<>();
		fitnessFunctions.put((individual) -> Evaluator.EdgeValue(individual), 0.1);
		fitnessFunctions.put((individual) -> Evaluator.ConnectivityMeasure(individual), 200.0);
		fitnessFunctions.put((individual) -> Evaluator.OverallDeviation(individual), 0.1);

		RunConfig config = new RunConfig();

		SGA ga = new SGA(config.mutationRate, config.crossoverRate, fitnessFunctions, config.populationSize);
		try {
			BufferedImage image = ImageIO.read(new File(config.dataFile));

			ga.run(image, config.nbrGenerations);

			System.out.println("Done");

			Individual best = (Individual) ga.getBestIndividual();
			System.out.println(best.getClusters().size());
            best.printClusters();

			Image segmented = new Image(image, best);
			segmented.outputSegmentedImages(config.outputPath, config.dataFile);

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("ERROR");
			e.printStackTrace();
		}
	}
}
