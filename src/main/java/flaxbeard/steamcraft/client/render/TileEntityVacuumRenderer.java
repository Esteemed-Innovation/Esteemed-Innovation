package flaxbeard.steamcraft.client.render;

import flaxbeard.steamcraft.block.BlockVacuum;
import flaxbeard.steamcraft.client.render.model.ModelVacuum;
import flaxbeard.steamcraft.tile.TileEntityVacuum;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class TileEntityVacuumRenderer extends TileEntitySpecialRenderer implements IInventoryTESR {
    private static final ModelVacuum model = new ModelVacuum();
    private static final ResourceLocation fanTexture = new ResourceLocation("steamcraft:textures/models/fan.png");

    private Runnable renderSide = new Runnable() { @Override public void run() { model.renderSide(); }};
    private Runnable renderBlade = new Runnable() { @Override public void run() { model.renderBlade(); }};

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float partialTicks, int destroyStage) {
        TileEntityVacuum vacuum = (TileEntityVacuum) tile;

        GL11.glPushMatrix();
        GL11.glTranslatef((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F);
        EnumFacing facing = tile.getWorld().getBlockState(tile.getPos()).getValue(BlockVacuum.FACING);
        switch (facing) {
            case DOWN: {
                GL11.glRotatef(-90F, 0F, 0F, 1F);
            }
            case UP: {
                GL11.glRotatef(90F, 0F, 0F, 1F);
            }
            case NORTH: {
                GL11.glRotatef(90F, 0F, 1F, 0F);
            }
            case SOUTH: {
                GL11.glRotatef(-90F, 0F, 1F, 0F);
            }
            case WEST: {
                GL11.glRotatef(180F, 0F, 1F, 0F);
            }
        }
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);

        Minecraft.getMinecraft().renderEngine.bindTexture(fanTexture);
        model.render();
        renderFour(renderSide);
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        GL11.glRotatef(-vacuum.rotateTicks * 25.0F, 1F, 0F, 0F);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);

        model.renderPole();
        renderFour(renderBlade);

        GL11.glPopMatrix();

    }

    private void renderFour(Runnable render) {
        for (int i = 0; i < 4; i++) {
            GL11.glPushMatrix();
            GL11.glTranslatef(0.5F, 0.5F, 0.5F);
            GL11.glRotatef(90F * i, 1F, 0F, 0F);
            GL11.glRotatef(10F, 0F, 1F, 0F);
            GL11.glTranslatef(-0.5F, -0.5F, -0.5F);

            // Hey Striking, you like this functional programming masterpiece?
            render.run();
            GL11.glPopMatrix();
        }
    }

    @Override
    public void renderInventoryTileEntityAt(TileEntity var1, double x, double y, double z, float var8) {
        GL11.glPushMatrix();
        GL11.glTranslated(x, y, z);

        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        Minecraft.getMinecraft().renderEngine.bindTexture(fanTexture);

        model.render();
        renderFour(renderSide);
        model.renderPole();
        renderFour(renderBlade);
        GL11.glPopMatrix();
    }
}
