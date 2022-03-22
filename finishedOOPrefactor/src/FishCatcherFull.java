import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class FishCatcherFull extends Dude implements Transformable{

    public FishCatcherFull(String id, Point position, List<PImage> images, int actionPeriod,
                           int animationPeriod, int resourceLimit){
        super(id, position, images, actionPeriod, animationPeriod, resourceLimit);

    }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        {
            Entity Obstacle = new Obstacle("obstacle", new Point(0, 0), 0, null);

            Optional<Entity> fullTarget =
                    world.findNearest(this.getPosition(), new ArrayList<>(Arrays.asList(Obstacle)));

            Entity x = (Obstacle) fullTarget.get();

            if (fullTarget.isPresent() && this.moveTo(world,
                    x, scheduler))
            {
                this.transform(world, scheduler, imageStore);
                Entity fish = new Fish("fish", new Point(x.getPosition().getX(), x.getPosition().getY()), imageStore.getImageList("Fish"), 50 , 50);
                world.addEntity(fish);
                Fish realFish = (Fish) fish;
                realFish.scheduleActions(scheduler, world, imageStore);
            }
            else {
                scheduler.scheduleEvent(this,
                        Activity.createAction(this, world, imageStore),
                        this.getActionPeriod());
            }
        }
    }

    public boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler) {
//        target = (Obstacle) target;

        if (this.adjacent(this.getPosition(), target.getPosition())) {
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

    public boolean transform(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        if(world.fishRemoved == 7){
            world.fishRemoved = 0;
            Entity dudeTree = world.createDudeNotFull("DudeNotFull", this.getPosition(), 50, 50, 150, imageStore.getImageList("dude"));
            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            Dude_NotFull realDude = ((Dude_NotFull) dudeTree);
            world.addEntity(realDude);
            realDude.scheduleActions(scheduler, world, imageStore);
            return true;
        }
        else {
            Entity x = world.createDudeFishingPoleNotFull("FishCatcherNotFull",
                    this.getPosition(), imageStore.getImageList("FishCatcherNotFull"),
                    this.getAnimationPeriod(),
                    this.getActionPeriod(),
                    1, 0, imageStore);

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);



            FishCatcherNotFull realDude = ((FishCatcherNotFull) x);
            world.addEntity(realDude);
            realDude.scheduleActions(scheduler, world, imageStore);
            return true;
        }

    }
}
