package no.oyvegard;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.imageio.ImageIO;
import javax.swing.event.SwingPropertyChangeSupport;

import no.oyvegard.GA.GAIndividual;
import no.oyvegard.GA.NSGA;

class Utils {
    public static void SegmentImage(BufferedImage image, String outputDir, OutputConfig outputConfig) {
        RunConfig config = new RunConfig();

        List<Function<GAIndividual, Double>> fitnessFunctions = new ArrayList<>();
        fitnessFunctions.add((individual) -> Evaluator.EdgeValue(individual));
        fitnessFunctions.add((individual) -> Evaluator.ConnectivityMeasure(individual));
        fitnessFunctions.add((individual) -> Evaluator.OverallDeviation(individual));

        List<GAIndividual> totalPopulation = IntStream.range(0, config.nbrIslands).parallel().mapToObj(i -> {
            NSGA ga = new NSGA(config.mutationRate, config.crossoverRate, config.populationSize, fitnessFunctions);
            ga.run(image, config.nbrGenerations);
            // totalPopulation.addAll(ga.getPopulation());
            return ga.getPopulation();
        }).flatMap(Collection::stream).collect(Collectors.toList());

        NSGA finalGa = new NSGA(config.mutationRate, config.crossoverRate, config.populationSize, fitnessFunctions);
        finalGa.setPopulation(totalPopulation);

        List<GAIndividual> candidates = finalGa.getBestIndividuals(5);

        for (int i = 0; i < candidates.size(); i++) {
            GAIndividual s = candidates.get(i);
            Image segmented = new Image(image, (Individual) s);

            String out = outputDir + "/output_" + i + "_segmented";

            switch (outputConfig) {
                case BOTH:
                    segmented.drawSegmentedImage(1, out + "_1.jpg");
                case TYPE_TWO_ONLY:
                    segmented.drawSegmentedImage(2, out + "_2.jpg");
                    break;
            }
        }
    }

    public static void SegmentImageForDemo() {
        RunConfig config = new RunConfig();
        File outputFolder = new File(config.outputPath);
        File optimalFolder = new File(config.optimalPath);

        File sourceFolder = new File("training_images/" + config.trainName);

        deleteFolder(outputFolder);
        deleteFolder(optimalFolder);
        copyGT(sourceFolder, optimalFolder);

        try {
            BufferedImage image = ImageIO.read(new File("training_images/" + config.trainName + "/Test image.jpg"));
            SegmentImage(image, config.outputPath, OutputConfig.TYPE_TWO_ONLY);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static void SegmentAllImagesBothTypes() {
        File outputFolder = new File("segmented_images");
        if (!outputFolder.exists()) {
            outputFolder.mkdir();
        }

        File sourceFolder = new File("training_images/");

        deleteFolder(outputFolder);

        File[] files = sourceFolder.listFiles();

        for (File folder : files) {
            if (!folder.isDirectory()) {
                continue;
            }
            for (File file : folder.listFiles()) {
                if (file.getName().contains("Test")) {
                    try {
                        System.out.println("Predicting image: " + file.getName());
                        BufferedImage image = ImageIO.read(file);
                        File out = new File(outputFolder + "/" + folder.getName());
                        if (!out.exists()) {
                            out.mkdirs();
                        }

                        deleteFolder(out);
                        SegmentImage(image, outputFolder + "/" + out.getName(), OutputConfig.BOTH);
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

            }
        }
    }

    public static void copyGT(File sourceFolder, File outputFolder) {
        File[] files = sourceFolder.listFiles();
        for (File file : files) {
            if (file.getName().contains("GT")) {
                try {
                    Files.copy(file.toPath(), new File(outputFolder, file.getName()).toPath());
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    public static void deleteFolder(File folder) {
        File[] files = folder.listFiles();
        if (files != null) { // some JVMs return null for empty dirs
            for (File f : files) {
                if (f.isDirectory()) {
                    deleteFolder(f);
                } else {
                    f.delete();
                }
            }
        }
    }

}
