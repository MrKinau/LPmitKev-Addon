package de.lpmitkev.kinau.lasertag;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.List;

public class LaserUpdateTickListener {

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        List<Laser> toRemove = new ArrayList<Laser>();
        for (Laser laser : Laser.currentLasers) {
            laser.setTicksLeft(laser.getTicksLeft() - 1);
            if (laser.isFinished())
                toRemove.add(laser);
        }
        Laser.currentLasers.removeAll(toRemove);
        toRemove.clear();
    }
}
