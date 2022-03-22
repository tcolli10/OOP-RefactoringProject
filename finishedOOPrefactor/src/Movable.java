import processing.core.PImage;

import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public abstract class Movable extends Active{



    public Movable(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod) {
        super(id, position, images, animationPeriod, actionPeriod);
    }

    public boolean adjacent(Point p1, Point p2) {
        return (p1.getX() == p2.getX() && Math.abs(p1.getY() - p2.getY()) == 1) || (p1.getY() == p2.getY()
                && Math.abs(p1.getX() - p2.getX()) == 1);
    }

    abstract boolean moveTo(WorldModel world,
                            Entity target,
                            EventScheduler scheduler);



    public Point nextPosition(WorldModel world, Point destPos){
        Predicate<Point> canPassThroughFairy = point -> !world.isOccupied(point) && world.withinBounds(point);
        Predicate<Point> canPassThroughDude = point -> world.withinBounds(point) && (!world.isOccupied(point) || world.getOccupancyCell(point) instanceof Stump);
        BiPredicate<Point, Point> withinReach = (p1, p2) -> adjacent(p1, p2);
//        PathingStrategy pathing = new SingleStepPathingStrategy();
        PathingStrategy pathing = new AStarPathingStrategy();
        List<Point> path;
        if(this.getClass() == Fairy.class){
            path = pathing.computePath(this.getPosition(), destPos, canPassThroughFairy, withinReach ,PathingStrategy.CARDINAL_NEIGHBORS);
        }
        else{
            path = pathing.computePath(this.getPosition(), destPos, canPassThroughDude, withinReach ,PathingStrategy.CARDINAL_NEIGHBORS);
        }

        if (path.size() == 0){
            return this.getPosition();
        }
        else{
            return path.get(0);
        }
    }

}
