public class Animation implements Action {
    private Animated entity;
    private int repeatCount;

    public Animation(
            Animated entity,
            int repeatCount)
    {
        this.entity = entity;
        this.repeatCount = repeatCount;
    }


    public void executeAction(EventScheduler scheduler) {
        this.entity.nextImage();

        if (this.repeatCount != 1) {
            scheduler.scheduleEvent(this.entity,
                    createAction(this.entity,
                            Math.max(this.repeatCount - 1,
                                    0)),
                    this.entity.getAnimationPeriod());
        }

        Point p = new Point(2, 3);
    }

    public static Animation createAction(Animated entity, int repeatCount) {
        return new Animation(entity, repeatCount);
    }




}
