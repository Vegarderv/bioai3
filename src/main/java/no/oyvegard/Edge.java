package no.oyvegard;

public class Edge {
    Piksel from;
    Piksel to;
    double distance;
    Direction direction;
    
    public Edge(Piksel from, Piksel to, double distance) {
        this.from = from;
        this.to = to;
        this.distance = distance;
        if (from.getX() == to.getX() - 1) {
            direction = Direction.LEFT;
        }
        else if (from.getX() == to.getX() + 1) {
            direction = Direction.RIGHT;
        }
        else if (from.getY() == to.getY() - 1) {
            direction = Direction.UP;
        }
        else {
            direction = Direction.DOWN;
        }
    }

    public double getDistance() {
        return distance;
    }

    public Piksel getFrom() {
        return from;
    }

    public Piksel getTo() {
        return to;
    }

    
}
