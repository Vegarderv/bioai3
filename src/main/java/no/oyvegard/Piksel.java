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

    public List<List<Integer>> getNeighbourIndices(int imageWidht, int imageHeight) {
        List<List<Integer>> neighbours = new ArrayList<>();
        if (this.x > 0) {
            neighbours.add(Arrays.asList(this.x - 1, this.y));
        }
        if (this.x < imageHeight - 1) {
            neighbours.add(Arrays.asList(this.x + 1, this.y));
        }
        if (this.y > 0) {
            neighbours.add(Arrays.asList(this.x, this.y - 1));
        }
        if (this.y < imageHeight - 1) {
            neighbours.add(Arrays.asList(this.x, this.y + 1));
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
        return ((red&0x0ff)<<16)|((green&0x0ff)<<8)|(blue&0x0ff);
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

}
