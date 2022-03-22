import processing.core.PImage;

import java.util.*;

public class Tree extends Active implements Plant, Transformable
{
    private int health;
    public Tree(

            String id,
            Point position,
            List<PImage> images,
            int actionPeriod,
            int animationPeriod,
            int health)
    {

        super(id, position, images, actionPeriod, animationPeriod);
        this.health = health;
    }

    public int getHealth(){ return this.health; }
    public void setHealth(int health){ this.health = health; }

    public boolean transform(
            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore)
    {
        if (this.getHealth() <= 0) {
            Entity stump = world.createStump(this.getId(),
                    this.getPosition(),
                    imageStore.getImageList(world.getStumpKey()));

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);
            world.addEntity(stump);
            return true;
        }

        return false;
    }


    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        if (!this.transform(world, scheduler, imageStore)) {
            scheduler.scheduleEvent(this,
                    Activity.createAction(this, world, imageStore),
                    this.getActionPeriod());
        }
    }

}
