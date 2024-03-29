package no.oyvegard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Piksel {

    private Direction direction;
    private int red;

    private int green;

    private int blue;

    private int x;
    private int y;
    private boolean isBorder = false;
    private boolean isVisited = false;

    public Piksel(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Piksel(int x, int y, int RGB) {
        this.x = x;
        this.y = y;
        red = (RGB >> 16) & 0xff;
        green = (RGB >> 8) & 0xff;
        blue = RGB & 0xff;
    }

    private int clusterIndex = -1;

    public void mutate(int imageWidht, int imageHeight) {
        List<Direction> directions = new ArrayList<>();
        if (x != 0) {
            directions.add(Direction.LEFT);
        }
        if (x != imageWidht - 1) {
            directions.add(Direction.RIGHT);
        }
        if (y != 0) {
            directions.add(Direction.UP);
        }
        if (y != imageHeight - 1) {
            directions.add(Direction.DOWN);
        }
        Random rand = new Random();
        this.direction = directions.get(rand.nextInt(directions.size()));
    }

    public List<List<Integer>> getNeighbourIndices(int imageWidth, int imageHeight) {
        List<List<Integer>> neighbours = new ArrayList<>();
        final int[][] directions = new int[][] { { 0, -1 }, { 0, 1 }, { -1, 0 }, { 1, 0 }, { -1, -1 }, { -1, 1 },
                { 1, -1 },
                { 1, 1 } };

        for (int[] direction : directions) {
            int x = this.x + direction[0];
            int y = this.y + direction[1];
            if (x >= 0 && x < imageWidth && y >= 0 && y < imageHeight) {
                neighbours.add(Arrays.asList(x, y));
            }
        }
        return neighbours;
    }

    public int getRed() {
        return red;
    }

    public void setRed(int red) {
        this.red = red;
    }

    public int getX() {
        return x;
    }

    public int getGreen() {
        return green;
    }

    public void setGreen(int green) {
        this.green = green;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getBlue() {
        return blue;
    }

    public void setBlue(int blue) {
        this.blue = blue;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean getVisited() {
        return this.isVisited;
    }

    public void setVisited(boolean isVisited) {
        this.isVisited = isVisited;
    }

    public void setRGB(int r, int g, int b) {
        this.red = r;
        this.green = g;
        this.blue = b;
    }

    public int getRGB() {
        return ((red & 0x0ff) << 16) | ((green & 0x0ff) << 8) | (blue & 0x0ff);
    }

    public boolean getIsBorder() {
        return isBorder;
    }

    public void setIsBorder(boolean isBorder) {
        this.isBorder = isBorder;
    }

    public int getClusterIndex() {
        return clusterIndex;
    }

    public void setClusterIndex(int clusterIndex) {
        this.clusterIndex = clusterIndex;
    }

    public void setDirection(Direction dir) {
        this.direction = dir;
    }

    public Direction getDirection() {
        return this.direction;
    }

    public boolean isVisited() {
        return this.direction != null;
    }

    @Override
    public boolean equals(Object obj) {
        if (!obj.getClass().equals(Piksel.class)) {
            throw new IllegalArgumentException("Must compare piksel with piksel");
        }
        Piksel other = (Piksel) obj;
        return this.x == other.getX() && this.y == other.getY();
    }

    public Piksel clone() {
        Piksel clone = new Piksel(this.x, this.y);
        clone.setRGB(this.red, this.green, this.blue);
        clone.setIsBorder(this.isBorder);
        clone.setVisited(this.isVisited);
        clone.setDirection(this.direction);
        clone.setClusterIndex(this.clusterIndex);
        return clone;
    }

}
