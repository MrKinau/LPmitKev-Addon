package de.lpmitkev.kinau.lasertag;

import de.lpmitkev.kinau.utils.Location;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;

import java.util.List;

public class LaserRenderer {

    private static final ResourceLocation beaconBeam = new ResourceLocation("lpmitkevde/textures/beacon_beam.png");
    private static final float LASER_RADIUS = 0.015F;
    private static final float LASER_GLOW_RADIUS = 0.04F;
    private static final float LASER_GLOW_ALPHA = 0.125F;

    public void render(List<Laser> lasers, Entity viewPort, float partialTicks) {
        if (lasers.isEmpty())
            return;

        double d0 = viewPort.prevPosX + (viewPort.posX - viewPort.prevPosX) * (double) partialTicks;
        double d1 = viewPort.prevPosY + (viewPort.posY - viewPort.prevPosY) * (double) partialTicks;
        double d2 = viewPort.prevPosZ + (viewPort.posZ - viewPort.prevPosZ) * (double) partialTicks;
        Vec3 pos = new Vec3(d0, d1, d2);
        GL11.glTranslated(-pos.xCoord, -pos.yCoord, -pos.zCoord);
        GlStateManager.disableFog();
        for (Laser laser : lasers) {
            if (laser.getStart() == null || laser.getStop() == null)
                return;

            Location from;
            if (laser.getTicksLeft() > 0)
                from = laser.getStart();
            else if (laser.getAnimatedLocations().size() > 0)
                from = laser.getAnimatedLocations().pop();
            else {
                laser.setFinished(true);
                continue;
            }

            double x = from.getX();
            double y = from.getY();
            double z = from.getZ();
            Location to = laser.getStop();
            double xTo = to.getX();
            double yTo = to.getY();
            double zTo = to.getZ();

            float colRed = laser.getColor().getRed() / 255.0F;
            float colGreen = laser.getColor().getGreen() / 255.0F;
            float colBlue = laser.getColor().getBlue() / 255.0F;

            GlStateManager.alphaFunc(516, 0.1F);
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldrenderer = tessellator.getWorldRenderer();

            Minecraft.getMinecraft().getTextureManager().bindTexture(beaconBeam);
            GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, 10497.0F);
            GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, 10497.0F);

            GlStateManager.disableLighting();
            GlStateManager.disableCull();
            GlStateManager.disableBlend();
            GlStateManager.depthMask(true);
            GlStateManager.tryBlendFuncSeparate(770, 1, 1, 0);

            worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);

            worldrenderer.pos(xTo - LASER_RADIUS, yTo - LASER_RADIUS, zTo + LASER_RADIUS).tex(0.4375, 0.0625).color(colRed, colGreen, colBlue, 1.0F).endVertex();
            worldrenderer.pos(xTo + LASER_RADIUS, yTo - LASER_RADIUS, zTo - LASER_RADIUS).tex(0.5625, 0.0625).color(colRed, colGreen, colBlue, 1.0F).endVertex();
            worldrenderer.pos(x + LASER_RADIUS, y - LASER_RADIUS, z - LASER_RADIUS).tex(0.5625, 0.9375).color(colRed, colGreen, colBlue, 1.0F).endVertex();
            worldrenderer.pos(x - LASER_RADIUS, y - LASER_RADIUS, z + LASER_RADIUS).tex(0.4375, 0.9375).color(colRed, colGreen, colBlue, 1.0F).endVertex();

            worldrenderer.pos(xTo - LASER_RADIUS, yTo - LASER_RADIUS, zTo + LASER_RADIUS).tex(0.4375, 0.0625).color(colRed, colGreen, colBlue, 1.0F).endVertex();
            worldrenderer.pos(xTo - LASER_RADIUS, yTo + LASER_RADIUS, zTo + LASER_RADIUS).tex(0.5625, 0.0625).color(colRed, colGreen, colBlue, 1.0F).endVertex();
            worldrenderer.pos(x - LASER_RADIUS, y + LASER_RADIUS, z + LASER_RADIUS).tex(0.5625, 0.9375).color(colRed, colGreen, colBlue, 1.0F).endVertex();
            worldrenderer.pos(x - LASER_RADIUS, y - LASER_RADIUS, z + LASER_RADIUS).tex(0.4375, 0.9375).color(colRed, colGreen, colBlue, 1.0F).endVertex();

            worldrenderer.pos(xTo + LASER_RADIUS, yTo - LASER_RADIUS, zTo - LASER_RADIUS).tex(0.4375, 0.0625).color(colRed, colGreen, colBlue, 1.0F).endVertex();
            worldrenderer.pos(xTo + LASER_RADIUS, yTo + LASER_RADIUS, zTo - LASER_RADIUS).tex(0.5625, 0.0625).color(colRed, colGreen, colBlue, 1.0F).endVertex();
            worldrenderer.pos(x + LASER_RADIUS, y + LASER_RADIUS, z - LASER_RADIUS).tex(0.5625, 0.9375).color(colRed, colGreen, colBlue, 1.0F).endVertex();
            worldrenderer.pos(x + LASER_RADIUS, y - LASER_RADIUS, z - LASER_RADIUS).tex(0.4375, 0.9375).color(colRed, colGreen, colBlue, 1.0F).endVertex();

            worldrenderer.pos(xTo - LASER_RADIUS, yTo + LASER_RADIUS, zTo + LASER_RADIUS).tex(0.4375, 0.0625).color(colRed, colGreen, colBlue, 1.0F).endVertex();
            worldrenderer.pos(xTo + LASER_RADIUS, yTo + LASER_RADIUS, zTo - LASER_RADIUS).tex(0.5625, 0.0625).color(colRed, colGreen, colBlue, 1.0F).endVertex();
            worldrenderer.pos(x + LASER_RADIUS, y + LASER_RADIUS, z - LASER_RADIUS).tex(0.5625, 0.9375).color(colRed, colGreen, colBlue, 1.0F).endVertex();
            worldrenderer.pos(x - LASER_RADIUS, y + LASER_RADIUS, z + LASER_RADIUS).tex(0.4375, 0.9375).color(colRed, colGreen, colBlue, 1.0F).endVertex();
            tessellator.draw();

            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.depthMask(false);

            worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);

            worldrenderer.pos(xTo - LASER_GLOW_RADIUS, yTo - LASER_GLOW_RADIUS, zTo + LASER_GLOW_RADIUS).tex(0.4375, 0.0625).color(colRed, colGreen, colBlue, LASER_GLOW_ALPHA).endVertex();
            worldrenderer.pos(xTo + LASER_GLOW_RADIUS, yTo - LASER_GLOW_RADIUS, zTo - LASER_GLOW_RADIUS).tex(0.5625, 0.0625).color(colRed, colGreen, colBlue, LASER_GLOW_ALPHA).endVertex();
            worldrenderer.pos(x + LASER_GLOW_RADIUS, y - LASER_GLOW_RADIUS, z - LASER_GLOW_RADIUS).tex(0.5625, 0.9375).color(colRed, colGreen, colBlue, LASER_GLOW_ALPHA).endVertex();
            worldrenderer.pos(x - LASER_GLOW_RADIUS, y - LASER_GLOW_RADIUS, z + LASER_GLOW_RADIUS).tex(0.4375, 0.9375).color(colRed, colGreen, colBlue, LASER_GLOW_ALPHA).endVertex();

            worldrenderer.pos(xTo - LASER_GLOW_RADIUS, yTo - LASER_GLOW_RADIUS, zTo + LASER_GLOW_RADIUS).tex(0.4375, 0.0625).color(colRed, colGreen, colBlue, LASER_GLOW_ALPHA).endVertex();
            worldrenderer.pos(xTo - LASER_GLOW_RADIUS, yTo + LASER_GLOW_RADIUS, zTo + LASER_GLOW_RADIUS).tex(0.5625, 0.0625).color(colRed, colGreen, colBlue, LASER_GLOW_ALPHA).endVertex();
            worldrenderer.pos(x - LASER_GLOW_RADIUS, y + LASER_GLOW_RADIUS, z + LASER_GLOW_RADIUS).tex(0.5625, 0.9375).color(colRed, colGreen, colBlue, LASER_GLOW_ALPHA).endVertex();
            worldrenderer.pos(x - LASER_GLOW_RADIUS, y - LASER_GLOW_RADIUS, z + LASER_GLOW_RADIUS).tex(0.4375, 0.9375).color(colRed, colGreen, colBlue, LASER_GLOW_ALPHA).endVertex();

            worldrenderer.pos(xTo + LASER_GLOW_RADIUS, yTo - LASER_GLOW_RADIUS, zTo - LASER_GLOW_RADIUS).tex(0.4375, 0.0625).color(colRed, colGreen, colBlue, LASER_GLOW_ALPHA).endVertex();
            worldrenderer.pos(xTo + LASER_GLOW_RADIUS, yTo + LASER_GLOW_RADIUS, zTo - LASER_GLOW_RADIUS).tex(0.5625, 0.0625).color(colRed, colGreen, colBlue, LASER_GLOW_ALPHA).endVertex();
            worldrenderer.pos(x + LASER_GLOW_RADIUS, y + LASER_GLOW_RADIUS, z - LASER_GLOW_RADIUS).tex(0.5625, 0.9375).color(colRed, colGreen, colBlue, LASER_GLOW_ALPHA).endVertex();
            worldrenderer.pos(x + LASER_GLOW_RADIUS, y - LASER_GLOW_RADIUS, z - LASER_GLOW_RADIUS).tex(0.4375, 0.9375).color(colRed, colGreen, colBlue, LASER_GLOW_ALPHA).endVertex();

            worldrenderer.pos(xTo - LASER_GLOW_RADIUS, yTo + LASER_GLOW_RADIUS, zTo + LASER_GLOW_RADIUS).tex(0.4375, 0.0625).color(colRed, colGreen, colBlue, LASER_GLOW_ALPHA).endVertex();
            worldrenderer.pos(xTo + LASER_GLOW_RADIUS, yTo + LASER_GLOW_RADIUS, zTo - LASER_GLOW_RADIUS).tex(0.5625, 0.0625).color(colRed, colGreen, colBlue, LASER_GLOW_ALPHA).endVertex();
            worldrenderer.pos(x + LASER_GLOW_RADIUS, y + LASER_GLOW_RADIUS, z - LASER_GLOW_RADIUS).tex(0.5625, 0.9375).color(colRed, colGreen, colBlue, LASER_GLOW_ALPHA).endVertex();
            worldrenderer.pos(x - LASER_GLOW_RADIUS, y + LASER_GLOW_RADIUS, z + LASER_GLOW_RADIUS).tex(0.4375, 0.9375).color(colRed, colGreen, colBlue, LASER_GLOW_ALPHA).endVertex();
            tessellator.draw();

            GlStateManager.enableLighting();
            GlStateManager.enableTexture2D();
            GlStateManager.depthMask(true);
        }
//        GlStateManager.enableFog();
    }

}
