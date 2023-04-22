package no.oyvegard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Piksel {

    private Direction direction;
    private int red;

    private int green;

    private int blue;

    private int x;
    private int y;
    private boolean isBorder = false;

    public Piksel(int x, int y) {
        this.x = x;
        this.y = y;
    }

    private int clusterIndex = -1;

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
