import processing.core.PImage;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Fish extends Movable{

    public Fish(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod){
        super(id, position, images, actionPeriod, animationPeriod);
    }


    public boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler) {

//        for (int i = 0; i < 10000000; i++)
//            while (world.getOccupancyCell(new Point(this.getPosition().getX() + 1, this.getPosition().getY())) instanceof Obstacle) {
//                this.setPosition(new Point(this.getPosition().getX() + 1, this.getPosition().getY()));
//            }
////            while (world.getOccupancyCell(new Point(this.getPosition().getX(), this.getPosition().getY() + 1)) instanceof Obstacle) {
//            this.setPosition(new Point(this.getPosition().getX(), this.getPosition().getY() + 1));
////            }

        return false;
    }



    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {

//            moveTo(world, null, scheduler );

    }


}
