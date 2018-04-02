package de.lpmitkev.kinau.utils;

import net.minecraft.util.Vec3;

public class Location {

    private double x, y, z;

    public Location(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public Vec3 toVec() {
        return new Vec3(x, y, z);
    }
}
