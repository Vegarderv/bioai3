package no.oyvegard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Piksel {

    private Direction direction;
    private int x;
    private int y;

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

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
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
