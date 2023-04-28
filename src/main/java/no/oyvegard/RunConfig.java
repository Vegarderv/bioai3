package no.oyvegard;

enum RunMode {
    OUTPUT_ALL,
    DEMO_PREDICT,
    COMPARE_NSGA_GA,
}

class RunConfig {
    int nbrIslands = 5;
    int populationSize = 10;
    int nbrGenerations = 150;
    float mutationRate = 0.001f;
    float crossoverRate = 0.05f;
    String dataFile = "training_images/86016/Test image.jpg";
    String outputPath = "evaluator/student_segments/";
    String optimalPath = "evaluator/optimal_segments/";
    String trainName = "176039";
    RunMode runMode = RunMode.OUTPUT_ALL;
}
