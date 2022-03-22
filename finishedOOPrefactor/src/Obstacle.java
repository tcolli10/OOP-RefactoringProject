import processing.core.PImage;

import java.util.*;

public class Obstacle extends Animated{

    public Obstacle(
            String id,
            Point position,
            int animationPeriod,
            List<PImage> images)

    {
        super(id, position, images, animationPeriod);
    }

    @Override
    public boolean equals(Object other){
        if(other == null){
            return false;
        }
        if(other == this){
            return true;
        }
        if(other.getClass() != this.getClass()){
            return false;
        }
        return true;
    }

}
