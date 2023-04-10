package no.oyvegard;

import java.util.List;

public class Piksel {
    
    private Direction direction;

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
