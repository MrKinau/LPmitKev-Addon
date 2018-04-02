package de.lpmitkev.kinau.lasertag;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class LaserRenderTickListener {

    private LaserRenderer laserRenderer = new LaserRenderer();

    @SubscribeEvent
    public void onRenderTick(RenderWorldLastEvent event) {
        laserRenderer.render(Laser.currentLasers, Minecraft.getMinecraft().getRenderViewEntity(), event.partialTicks);
    }
}
