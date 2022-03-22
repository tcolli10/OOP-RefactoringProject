import processing.core.PImage;

import java.util.List;

public abstract class Active extends Animated{

    private int actionPeriod;
    public Active(

            String id,
            Point position,
            List<PImage> images,
            int animationPeriod,
            int actionPeriod)

    {
        super(id, position, images, animationPeriod);
        this.actionPeriod = actionPeriod;
    }

//    public int getResourceLimit(){ return this.resourceLimit; }
//
//    public int getResourceCount(){ return this.resourceCount; }
//
//    public void setResourceCount(int count) {
//        this.resourceCount = count;
//    }

    public int getActionPeriod() {
        return actionPeriod;
    }

    abstract void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler);

    @Override
    public void scheduleActions(
            EventScheduler scheduler,
            WorldModel world,
            ImageStore imageStore)
    {

        scheduler.scheduleEvent(this,
                Activity.createAction(this, world, imageStore),
                this.actionPeriod);
        scheduler.scheduleEvent(this,
                Animation.createAction(this, 0),
                this.getAnimationPeriod());

    }


}
