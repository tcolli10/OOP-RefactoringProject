public class Activity implements Action{
    private Active entity;
    private WorldModel world;
    private ImageStore imageStore;

    public Activity(
            Active entity,
            WorldModel world,
            ImageStore imageStore)
    {
        this.entity = entity;
        this.world = world;
        this.imageStore = imageStore;
    }


    public void executeAction(EventScheduler scheduler) {
        this.entity.executeActivity(this.world,
                    this.imageStore, scheduler);

    }

    public static Activity createAction(Active entity, WorldModel world, ImageStore imageStore) {
        return new Activity(entity, world, imageStore);
    }




}
