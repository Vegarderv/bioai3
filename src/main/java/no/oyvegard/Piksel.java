package no.oyvegard;

public class Piksel {

    private Direction direction;
    private int x;
    private int y;

    public Piksel(int x, int y) {
        this.x = x;
        this.y = y;
    }

    private int clusterIndex = -1;

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
