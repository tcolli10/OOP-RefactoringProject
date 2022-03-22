import processing.core.PImage;

import java.util.*;

public class Sapling extends Active implements Plant, Transformable{

    private int healthLimit;
    private int health;
    private static final int TREE_ANIMATION_MAX = 600;
    private static final int TREE_ANIMATION_MIN = 50;
    private static final int TREE_ACTION_MAX = 1400;
    private static final int TREE_ACTION_MIN = 1000;
    private static final int TREE_HEALTH_MAX = 3;
    private static final int TREE_HEALTH_MIN = 1;

    public Sapling(
            String id,
            Point position,
            List<PImage> images,
            int actionPeriod,
            int animationPeriod,
            int health,
            int healthLimit)
    {
        super(id, position, images, actionPeriod, animationPeriod);
        this.health = health;
        this.healthLimit = healthLimit;
    }

    public int getHealth(){ return this.health; }
    public void setHealth(int health){ this.health = health; }

    public boolean transform(
            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore)
    {
        if (this.health <= 0) {
            Entity stump = world.createStump(this.getId(),
                    this.getPosition(),
                    imageStore.getImageList(world.getStumpKey()));

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);
            world.addEntity(stump);
            return true;
        }
        else if (this.health >= this.healthLimit)
        {
            Entity tree = world.createTree("tree_" + this.getId(),
                    this.getPosition(),
                    this.getNumFromRange(TREE_ACTION_MAX, TREE_ACTION_MIN),
                    this.getNumFromRange(TREE_ANIMATION_MAX, TREE_ANIMATION_MIN),
                    this.getNumFromRange(TREE_HEALTH_MAX,TREE_HEALTH_MIN),
                    imageStore.getImageList(world.getTreeKey()));

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(tree);
            Tree realTree = ((Tree)(tree));
            realTree.scheduleActions(scheduler, world, imageStore);

            return true;
        }

        return false;
    }

    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        this.health ++;
        if (!this.transform(world, scheduler, imageStore))
        {
            scheduler.scheduleEvent(this,
                    Activity.createAction(this, world, imageStore),
                    this.getActionPeriod());
        }
    }


    private int getNumFromRange(int max, int min)
    {
        Random rand = new Random();
        return min + rand.nextInt(
                max
                        - min);
    }

}
