package no.oyvegard;

import java.util.ArrayList;
import java.util.List;

public class Individual {

    private List<List<Piksel>> pixels;
    private int width;
    private int height;

    public Individual(int width, int height) {
        this.width = width;
        this.height = height;
        generateBoard();
    }

    private void generateBoard() {
        List<List<Piksel>> board = new ArrayList<>();
        for (int i = 0; i < height; i++) {
            List<Piksel> row = new ArrayList<>();
            for (int j = 0; j < width; j++) {
                row.add(new Piksel());
            }
            board.add(row);
        }
        this.pixels = board;
    }

    public List<List<Piksel>> getPixels() {
        return pixels;
    }

    public void changeDirection(List<Integer> pixel, Direction direction) {
        pixels.get(pixel.get(1)).get(pixel.get(0)).setDirection(direction);
    }

    

    
}
