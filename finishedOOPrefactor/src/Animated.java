import processing.core.PImage;

import java.util.List;

public abstract class Animated extends  Entity{
    private int animationPeriod;

    public Animated(
            String id,
            Point position,
            List<PImage> images,
            int animationPeriod)

    {
        super(id, position, images);
        this.animationPeriod = animationPeriod;
    }

    public void scheduleActions(
            EventScheduler scheduler,
            WorldModel world,
            ImageStore imageStore)
    {
        scheduler.scheduleEvent(this,
                Animation.createAction(this, 0),
                this.getAnimationPeriod());
    }

    int getAnimationPeriod(){return this.animationPeriod;}

    public void nextImage() {
        this.setImageIndex((this.getImageIndex() + 1) % this.getImages().size());
    }
}
