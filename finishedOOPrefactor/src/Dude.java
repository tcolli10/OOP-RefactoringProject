import processing.core.PImage;

import java.util.List;

public abstract class Dude extends Movable {

    int resourceLimit;

    public Dude(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod, int resourceLimit) {
        super(id, position, images, animationPeriod, actionPeriod);
        this.resourceLimit = resourceLimit;
    }

    int getResourceLimit(){ return this.resourceLimit; }

}
