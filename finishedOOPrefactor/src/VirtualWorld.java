import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import processing.core.*;

public final class VirtualWorld extends PApplet
{
    private static final int TIMER_ACTION_PERIOD = 100;

    private static final int VIEW_WIDTH = 640;
    private static final int VIEW_HEIGHT = 480;
    private static final int TILE_WIDTH = 32;
    private static final int TILE_HEIGHT = 32;
    private static final int WORLD_WIDTH_SCALE = 2;
    private static final int WORLD_HEIGHT_SCALE = 2;

    private static final int VIEW_COLS = VIEW_WIDTH / TILE_WIDTH;
    private static final int VIEW_ROWS = VIEW_HEIGHT / TILE_HEIGHT;
    private static final int WORLD_COLS = VIEW_COLS * WORLD_WIDTH_SCALE;
    private static final int WORLD_ROWS = VIEW_ROWS * WORLD_HEIGHT_SCALE;

    private static final String IMAGE_LIST_FILE_NAME = "imagelist";
    private static final String DEFAULT_IMAGE_NAME = "background_default";
    private static final int DEFAULT_IMAGE_COLOR = 0x808080;

    private static final String FAST_FLAG = "-fast";
    private static final String FASTER_FLAG = "-faster";
    private static final String FASTEST_FLAG = "-fastest";
    private static final double FAST_SCALE = 0.5;
    private static final double FASTER_SCALE = 0.25;
    private static final double FASTEST_SCALE = 0.10;

    private static double timeScale = 1.0;

    private ImageStore imageStore;
    private WorldModel world;
    private WorldView view;
    private EventScheduler scheduler;

    private long nextTime;

    public void settings() {
        size(VIEW_WIDTH, VIEW_HEIGHT);
    }

    /*
       Processing entry point for "sketch" setup.
    */
    public void setup() {
        this.imageStore = new ImageStore(
                createImageColored(TILE_WIDTH, TILE_HEIGHT,
                                   DEFAULT_IMAGE_COLOR));
        this.world = new WorldModel(WORLD_ROWS, WORLD_COLS,
                                    createDefaultBackground(imageStore));
        this.view = new WorldView(VIEW_ROWS, VIEW_COLS, this, world, TILE_WIDTH,
                                  TILE_HEIGHT);
        this.scheduler = new EventScheduler(timeScale);

        loadImages(IMAGE_LIST_FILE_NAME, imageStore, this);
        String LOAD_FILE_NAME = "world.sav";
        loadWorld(world, LOAD_FILE_NAME, imageStore);

        scheduleActions(world, scheduler, imageStore);

        nextTime = System.currentTimeMillis() + TIMER_ACTION_PERIOD;
    }

    public void draw() {
        long time = System.currentTimeMillis();
        if (time >= nextTime) {
            this.scheduler.updateOnTime(time);
            nextTime = time + TIMER_ACTION_PERIOD;
        }

        view.drawViewport();
    }

    // Just for debugging and for P5
    // This should be refactored as appropriate
    public void mousePressed() {
//        world.fishRemoved = 0;
        Point pressed = mouseToPoint(mouseX, mouseY);
        Point pressedOG = pressed;
        System.out.println("CLICK! " + pressed.getX() + ", " + pressed.getY());

        Optional<Entity> entityOptional = world.getOccupant(pressed);

            if (entityOptional.isPresent()) {
                Entity entity = entityOptional.get();
                if (entity instanceof Plant){
                    System.out.println(entity.getId() + ": " + entity.getClass() + " : " + ((Plant)entity).getHealth());
                }
                else {
                    System.out.println(entity.getId() + ": " + entity.getClass());
                }

            }
            else {
                int xCounter = 0;
                List<Point> fishPositions = new ArrayList<>();
                Background fishBack = new Background("dirt", imageStore.getImageList("dirt"));
                for(int i = 0; i < 7; i++){
//                    Fish fish1 = new Fish("fish-" + pressed.getX() + "-" + pressed.getY(), pressed,
//                            this.imageStore.getImageList("Fish"), 50, 50);
                    FishOutttaWata fish1 = new FishOutttaWata("fish-" + pressed.getX() + "-" + pressed.getY(), pressed,
                            this.imageStore.getImageList("Fish"));
//                    Background fishBack = new Background("dirt", imageStore.getImageList("dirt"));
                    world.setBackgroundCell(fish1.getPosition(), fishBack);
                    this.world.addEntity(fish1);
                    fishPositions.add(new Point(pressed.getX(), pressed.getY()));
                    while (this.world.isOccupied(pressed)){
                        pressed = new Point(pressed.getX(), pressed.getY() + 1);
                        xCounter ++;
                        if(xCounter >= 3){
                            pressed = new Point(pressed.getX()+ 1, pressed.getY() );
                        }
                    }
                }
                for(int i = 0; i < 1; i++){
                Dude_NotFull finder = new Dude_NotFull("dudeNotFull", null, null, 0, 0, 0, 0);
                Optional<Entity> dude = world.findNearest(pressedOG,new ArrayList<>(Arrays.asList(finder)));
                if(dude != null){
                    Dude_NotFull realDude = (Dude_NotFull) dude.get();
                    Point pos = realDude.getPosition();
                    world.removeEntity(realDude);
                    scheduler.unscheduleAllEvents(realDude);
                    FishCatcherNotFull newDude = new FishCatcherNotFull("FishCatcherNotFull",
                            pos, this.imageStore.getImageList("FishCatcherNotFull"),
                            50, 50, 1, 0, imageStore);
                    world.addEntity(newDude);
                    newDude.scheduleActions(scheduler, world, imageStore);
                }
//                    Dude_NotFull realDude = (Dude_NotFull) dude.get();
//                    Point pos = realDude.getPosition();
//                    world.removeEntity(realDude);
//                    scheduler.unscheduleAllEvents(realDude);
//                    DudeFishingPoleNotFull newDude = new DudeFishingPoleNotFull("dudeFishingPoleNotFull",
//                            pos, this.imageStore.getImageList("DudeFishingPoleNotFull"),
//                            50, 50, 1, 0);
//                    world.addEntity(newDude);
//                    newDude.scheduleActions(scheduler, world, imageStore);
                }
            }
    }

    private Point mouseToPoint(int x, int y)
    {
        return view.getViewport().viewportToWorld(mouseX/TILE_WIDTH, mouseY/TILE_HEIGHT);
    }
    public void keyPressed() {
        if (key == CODED) {
            int dx = 0;
            int dy = 0;

            switch (keyCode) {
                case UP:
                    dy = -1;
                    break;
                case DOWN:
                    dy = 1;
                    break;
                case LEFT:
                    dx = -1;
                    break;
                case RIGHT:
                    dx = 1;
                    break;
            }
            this.shiftView(view, dx, dy);
        }
    }

    private static Background createDefaultBackground(ImageStore imageStore) {
        return new Background(DEFAULT_IMAGE_NAME,
                imageStore.getImageList(DEFAULT_IMAGE_NAME));
    }

    private static PImage createImageColored(int width, int height, int color) {
        PImage img = new PImage(width, height, RGB);
        img.loadPixels();
        for (int i = 0; i < img.pixels.length; i++) {
            img.pixels[i] = color;
        }
        img.updatePixels();
        return img;
    }

    private static void loadImages(
            String filename, ImageStore imageStore, PApplet screen)
    {
        try {
            Scanner in = new Scanner(new File(filename));
            imageStore.loadImages(in, screen);
        }
        catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }

    private void loadWorld(
            WorldModel world, String filename, ImageStore imageStore)
    {
        try {
            Scanner in = new Scanner(new File(filename));
            this.load(in, world, imageStore);
        }
        catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }

    private static void scheduleActions(
            WorldModel world, EventScheduler scheduler, ImageStore imageStore)
    {
        for (Entity entity : world.getEntities()) {
            if (entity instanceof Active){
                ((Active)entity).scheduleActions(scheduler, world, imageStore);
            }
            else if (entity instanceof Obstacle){
                ((Obstacle) entity).scheduleActions(scheduler, world, imageStore);
            }
        }
    }

    private static void parseCommandLine(String[] args) {
        if (args.length > 1)
        {
            if (args[0].equals("file"))
            {

            }
        }
        for (String arg : args) {
            switch (arg) {
                case FAST_FLAG:
                    timeScale = Math.min(FAST_SCALE, timeScale);
                    break;
                case FASTER_FLAG:
                    timeScale = Math.min(FASTER_SCALE, timeScale);
                    break;
                case FASTEST_FLAG:
                    timeScale = Math.min(FASTEST_SCALE, timeScale);
                    break;
            }
        }
    }

    private void load(
            Scanner in, WorldModel world, ImageStore imageStore)
    {
        int lineNumber = 0;
        while (in.hasNextLine()) {
            try {
                if (!world.processLine(in.nextLine(), imageStore)) {
                    System.err.println(String.format("invalid entry on line %d",
                            lineNumber));
                }
            }
            catch (NumberFormatException e) {
                System.err.println(
                        String.format("invalid entry on line %d", lineNumber));
            }
            catch (IllegalArgumentException e) {
                System.err.println(
                        String.format("issue on line %d: %s", lineNumber,
                                e.getMessage()));
            }
            lineNumber++;
        }
    }

    private static void shiftView(WorldView view, int colDelta, int rowDelta) {
        int newCol = clamp(view.getViewport().getCol() + colDelta, 0,
                view.getWorld().getNumCols() - view.getViewport().getNumCols());
        int newRow = clamp(view.getViewport().getRow() + rowDelta, 0,
                view.getWorld().getNumRows() - view.getViewport().getNumRows());

        view.getViewport().shift(newCol, newRow);
    }

    private static int clamp(int value, int low, int high) {
        return Math.min(high, Math.max(value, low));
    }

    public static void main(String[] args) {
        parseCommandLine(args);
        PApplet.main(VirtualWorld.class);
    }
}
