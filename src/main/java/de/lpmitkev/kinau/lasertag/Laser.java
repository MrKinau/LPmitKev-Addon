package de.lpmitkev.kinau.lasertag;

import de.lpmitkev.kinau.utils.Location;
import net.minecraft.util.Vec3;
import org.lwjgl.util.Color;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class Laser {

    static final List<Laser> currentLasers = new ArrayList<Laser>();

    private Location start, stop;
    private Vec3 direction;
    private Stack<Location> animatedLocations = new Stack<Location>();
    private double length;
    private Color color;
    private long ticksLeft;
    private boolean finished = false;

    public Laser(Location start, Location end, Color color) {
        this.start = start;
        this.stop = end;
        this.color = color;
        this.ticksLeft = 15;
        this.length = start.toVec().distanceTo(stop.toVec());
        this.direction = stop.toVec().subtract(start.toVec()).normalize();
        this.direction = new Vec3(direction.xCoord * 1.5, direction.yCoord * 1.5, direction.zCoord * 1.5);
        calcAnimLocations();
        currentLasers.add(this);
    }

    public Location getStart() {
        return start;
    }

    public Location getStop() {
        return stop;
    }

    public Color getColor() {
        return color;
    }

    public long getTicksLeft() {
        return ticksLeft;
    }

    public Stack<Location> getAnimatedLocations() {
        return animatedLocations;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setTicksLeft(long ticksLeft) {
        this.ticksLeft = ticksLeft;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    private void calcAnimLocations() {
        double currentLength = 0;
        Location lastLoc = start;
        while (currentLength <= length) {
            lastLoc = new Location(lastLoc.getX() + direction.xCoord, lastLoc.getY() + direction.yCoord, lastLoc.getZ() + direction.zCoord);
            currentLength = start.toVec().distanceTo(lastLoc.toVec());
            if (currentLength < length)
                animatedLocations.push(lastLoc);
            else
                break;
        }
        Collections.reverse(animatedLocations);
    }
}
