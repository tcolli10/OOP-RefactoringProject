import processing.core.PImage;

import java.util.*;

public class Fairy extends Movable {

    public Fairy(

            String id,
            Point position,
            List<PImage> images,
            int actionPeriod,
            int animationPeriod)

    {
        super(id, position, images, actionPeriod, animationPeriod);
    }


    public boolean moveTo(
            WorldModel world,
            Entity target,
            EventScheduler scheduler)
    {
        if (this.adjacent(this.getPosition(), target.getPosition())) {
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);
            return true;
        }
        else {

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
            EventScheduler scheduler)
    {
        Entity Stump = new Stump("target", new Point(0,1), null);
        Optional<Entity> fairyTarget =
                world.findNearest(this.getPosition(), new ArrayList<>(Arrays.asList(Stump)));

        if (fairyTarget.isPresent()) {
            Point tgtPos = fairyTarget.get().getPosition();

            if (this.moveTo(world, fairyTarget.get(), scheduler)) {
                Entity sapling = world.createSapling("sapling_" + this.getId(), tgtPos,
                        imageStore.getImageList(world.getSaplingKey()));

                world.addEntity(sapling);
                Sapling realSapling = ((Sapling) sapling);
                realSapling.scheduleActions(scheduler, world, imageStore);
            }
        }

        scheduler.scheduleEvent(this,
                Activity.createAction(this, world, imageStore),
                this.getActionPeriod());
    }

}
