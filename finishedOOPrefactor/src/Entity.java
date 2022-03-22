import processing.core.PImage;

import java.util.List;

public abstract class Entity {

    public Entity(
            String id,
            Point position,
            List<PImage> images)
    {
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
    }

    private String id;
    private Point position;
    private List<PImage> images;
    private int imageIndex;

    Point getPosition(){ return this.position; }

    String getId(){ return this.id; }

    List<PImage> getImages(){ return this.images; }

    int getImageIndex(){ return this.imageIndex; }

    PImage getCurrentImage(){ return this.getImages().get(this.getImageIndex()); }

    public void setPosition(Point position){
        this.position = position;
    } // movable

    public void setImageIndex(int index){ this.imageIndex = index; }

}