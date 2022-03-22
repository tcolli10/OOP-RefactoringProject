import processing.core.PImage;

import java.util.*;

public class Dude_NotFull extends Dude implements Transformable{

    public Dude_NotFull(String id, Point position, List<PImage> images,
                        int actionPeriod, int animationPeriod, int resourceLimit, int resourceCount) {
        super(id, position, images, actionPeriod, animationPeriod, resourceLimit);
        this.resourceCount = resourceCount;
    }

    private int resourceCount;

    public boolean transform(
            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore) {
        if (this.resourceCount >= this.resourceLimit) {
            Dude_Full dudeFull = world.createDudeFull(this.getId(),
                    this.getPosition(), this.getAnimationPeriod(),
                    this.getActionPeriod(),
                    this.resourceLimit,
                    this.getImages());

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(dudeFull);
            dudeFull.scheduleActions(scheduler, world, imageStore);

            return true;
        }

        return false;
    }


    public boolean moveTo(
            WorldModel world,
            Entity target,
            EventScheduler scheduler) {
        Plant realTarget;
        if (target instanceof Tree) {
            realTarget = ((Tree) target);
        } else {
            realTarget = ((Sapling) target);
        }

        if (this.adjacent(this.getPosition(), target.getPosition())) {
            this.resourceCount += 1;
            realTarget.setHealth(realTarget.getHealth() - 1);
            return true;
        } else {
            Point nextPos = this.nextPosition(world, target.getPosition());

            if (!this.getPosition().equals(nextPos)) {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent()) {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                world.moveEntity(this, nextPos);
            }
            return false;
        }
    }


    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler) {
        Entity tree = new Tree("tree", new Point(0, 1), null, 0, 0,
                0);

        Entity sapling = new Sapling("sapling", new Point(0, 0), null, 0, 0,
                0, 0);

        Optional<Entity> target =
                world.findNearest(this.getPosition(), new ArrayList<>(Arrays.asList(tree, sapling)));

        if (!target.isPresent() || !this.moveTo(world,
                target.get(),
                scheduler)
                || !this.transform(world, scheduler, imageStore)) {
            scheduler.scheduleEvent(this,
                    Activity.createAction(this, world, imageStore),
                    this.getActionPeriod());
        }
    }

}
