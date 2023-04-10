package no.oyvegard.GA;

import java.util.List;

interface GAIndividual {
    public void mutate();
    public List<GAIndividual> crossover(GAIndividual other);
}
