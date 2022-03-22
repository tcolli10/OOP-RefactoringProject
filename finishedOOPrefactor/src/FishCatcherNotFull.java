import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class FishCatcherNotFull extends Dude implements Transformable{

    private int resourceCount;
    private ImageStore imageStore;
    public FishCatcherNotFull(String id, Point position, List<PImage> images, int actionPeriod,
                              int animationPeriod, int resourceLimit, int resourceCount, ImageStore imageStore){
        super(id, position, images, actionPeriod, animationPeriod, resourceLimit);
        this.resourceCount = resourceCount;
        this.imageStore = imageStore;
    }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {

        Entity fish = new FishOutttaWata("fish", new Point(0,0), null);
        Optional<Entity> target =
                world.findNearest(this.getPosition(), new ArrayList<>(Arrays.asList(fish)));

        if (!target.isPresent() || !this.moveTo(world,
                target.get(),
                scheduler)
                || !this.transform(world, scheduler, imageStore)) {
            scheduler.scheduleEvent(this,
                    Activity.createAction(this, world, imageStore),
                    this.getActionPeriod());
        }
    }

    public boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler) {

        target = (FishOutttaWata) target;
        Background fishGrass = new Background("grass", imageStore.getImageList("grass"));
        if (this.adjacent(this.getPosition(), target.getPosition())) {
            this.resourceCount += 1;
            world.fishRemoved ++;
            Point targetPos = target.getPosition();
            world.removeEntity(target);
            world.setBackgroundCell(targetPos, fishGrass);
            return true;
        } else {
            Point nextPos = this.nextPosition(world, target.getPosition());
            if (nextPos.equals(this.getPosition())){
                Entity dudeTree = world.createDudeNotFull("DudeNotFull", this.getPosition(), 50, 50, 150, imageStore.getImageList("dude"));
                world.removeEntity(this);
                scheduler.unscheduleAllEvents(this);

                Dude_NotFull realDude = ((Dude_NotFull) dudeTree);
                world.addEntity(realDude);
                realDude.scheduleActions(scheduler, world, imageStore);
            }
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

    public boolean transform(
            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore) {
        if (this.resourceCount >= this.resourceLimit) {
            FishCatcherFull dudeFishingPoleFull = world.createDudeFishingPoleFull(this.getId(),
                    this.getPosition(), imageStore.getImageList("FishCatcherFull"),
                    this.getAnimationPeriod(),
                    this.getActionPeriod(),
                    this.resourceLimit);

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(dudeFishingPoleFull);
            dudeFishingPoleFull.scheduleActions(scheduler, world, imageStore);

            return true;
        }

        return false;
    }
}
