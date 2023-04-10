package no.oyvegard;

import java.util.List;

public class Edge {
    List<Integer> from;
    List<Integer> to;
    double distance;
    Direction direction;
    
    public Edge(List<Integer> from, List<Integer> to, double distance) {
        this.from = from;
        this.to = to;
        this.distance = distance;
        if (from.get(0) == to.get(0) - 1) {
            direction = Direction.LEFT;
        }
        else if (from.get(0) == to.get(0) + 1) {
            direction = Direction.RIGHT;
        }
        else if (from.get(1) == to.get(1) - 1) {
            direction = Direction.UP;
        }
        else {
            direction = Direction.DOWN;
        }
    }

    public double getDistance() {
        return distance;
    }

    public List<Integer> getFrom() {
        return from;
    }

    public List<Integer> getTo() {
        return to;
    }

    
}
