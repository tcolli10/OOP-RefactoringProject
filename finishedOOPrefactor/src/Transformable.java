import processing.core.PImage;

import java.util.List;

public interface Transformable {

//    public Transformable(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod) {
//        super(id, position, images, actionPeriod, animationPeriod);
//    }

    boolean transform(
            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore);
}
