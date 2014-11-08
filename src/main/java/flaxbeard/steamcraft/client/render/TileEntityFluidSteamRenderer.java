package flaxbeard.steamcraft.client.render;

import flaxbeard.steamcraft.client.render.model.ModelFluidSteam;
import flaxbeard.steamcraft.tile.TileEntityFluidSteamConverter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class TileEntityFluidSteamRenderer extends TileEntitySpecialRenderer implements IInventoryTESR {
    private static final ModelFluidSteam model = new ModelFluidSteam();
    private static final ResourceLocation texture = new ResourceLocation("steamcraft:textures/models/fluidSteam.png");

    @Override
    public void renderTileEntityAt(TileEntity var1, double var2, double var4, double var6, float var8) {
        TileEntityFluidSteamConverter converter = (TileEntityFluidSteamConverter) var1;
        GL11.glPushMatrix();
        GL11.glTranslatef((float) var2 + 0.5F, (float) var4 + 0.5F, (float) var6 + 0.5F);
        int meta = var1.getWorldObj().getBlockMetadata(var1.xCoord, var1.yCoord, var1.zCoord);
        if (meta == 0) {
            GL11.glRotatef(-90.0F, 0F, 0F, 1F);
        }
        if (meta == 1) {
            GL11.glRotatef(90.0F, 0F, 0F, 1F);
        }
        if (meta == 3) {
            GL11.glRotatef(-90.0F, 0F, 1F, 0F);
        }
        if (meta == 2) {
            GL11.glRotatef(90.0F, 0F, 1F, 0F);
        }
        if (meta == 4) {
            GL11.glRotatef(180.0F, 0F, 1F, 0F);
        }
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        Minecraft.getMinecraft().renderEngine.bindTexture(texture);
        model.renderAnchored();
        GL11.glPushMatrix();
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        GL11.glTranslatef(0.25F, 0.0F, 0.0F);
        GL11.glScaled((0.2F + (Math.cos(Math.toRadians(-90.0F + (360.0F / 100.0F) * (100 - converter.runTicks)))) * 0.13F) / 0.2F, 1.0D, 1.0D);
        //	GL11.glTranslatef(-1.00F, 0.0F, 0.0F);
        //GL11.glTranslatef(0.5F, 0.0F, 0.0F);

        model.renderSquish();
        GL11.glPopMatrix();
        GL11.glTranslated(0.2F - (Math.cos(Math.toRadians(-90.0F + (360.0F / 100.0F) * (100 - converter.runTicks)))) * 0.2F, 0.0F, 0.0F);
        model.renderMoving();
        GL11.glPopMatrix();
    }

    @Override
    public void renderInventoryTileEntityAt(TileEntity var1, double var2, double var4, double var6, float var8) {
        GL11.glPushMatrix();
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);

        GL11.glTranslatef((float) var2 + 0.5F, (float) var4 + 0.5F, (float) var6 + 0.5F);
        int meta = 4;
        if (meta == 0) {
            GL11.glRotatef(-90.0F, 0F, 0F, 1F);
        }
        if (meta == 1) {
            GL11.glRotatef(90.0F, 0F, 0F, 1F);
        }
        if (meta == 3) {
            GL11.glRotatef(-90.0F, 0F, 1F, 0F);
        }
        if (meta == 2) {
            GL11.glRotatef(90.0F, 0F, 1F, 0F);
        }
        if (meta == 4) {
            GL11.glRotatef(180.0F, 0F, 1F, 0F);
        }
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);

        Minecraft.getMinecraft().renderEngine.bindTexture(texture);
        model.renderAnchored();
        GL11.glPushMatrix();
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        GL11.glTranslatef(0.25F, 0.0F, 0.0F);
        GL11.glScaled((0.2F + (Math.cos(Math.toRadians(-90.0F + (360.0F / 100.0F) * (100)))) * 0.13F) / 0.2F, 1.0D, 1.0D);
        //	GL11.glTranslatef(-1.00F, 0.0F, 0.0F);
        //GL11.glTranslatef(0.5F, 0.0F, 0.0F);

        model.renderSquish();
        GL11.glPopMatrix();
        GL11.glTranslated(0.2F - (Math.cos(Math.toRadians(-90.0F + (360.0F / 100.0F) * (100)))) * 0.2F, 0.0F, 0.0F);
        model.renderMoving();
        GL11.glPopMatrix();
    }

}
