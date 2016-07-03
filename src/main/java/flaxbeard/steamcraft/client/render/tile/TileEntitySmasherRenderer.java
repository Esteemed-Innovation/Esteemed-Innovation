package flaxbeard.steamcraft.client.render.tile;

import flaxbeard.steamcraft.block.BlockSmasher;
import flaxbeard.steamcraft.client.render.model.ModelSmasher;
import flaxbeard.steamcraft.tile.TileEntitySmasher;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class TileEntitySmasherRenderer extends TileEntitySpecialRenderer implements IInventoryTESR {
    private static final ModelSmasher MODEL = new ModelSmasher();
    private static final ResourceLocation TEXTURE = new ResourceLocation("steamcraft:textures/models/smasher.png");

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float partialTicks, int destroyStage) {
        TileEntitySmasher smasher = (TileEntitySmasher) tile;
        GL11.glPushMatrix();
        GL11.glTranslatef((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F);
        EnumFacing dir = tile.getWorld().getBlockState(tile.getPos()).getValue(BlockSmasher.FACING);
        switch (dir) {
            case DOWN: {
                GL11.glRotatef(90.0F, 0F, 1F, 0F);
                break;
            }
            case UP: {
                GL11.glRotatef(90.0F, 0F, 1F, 0F);
                break;
            }
            case NORTH: {
                GL11.glRotatef(90.0F, 0F, 1F, 0F);
                break;
            }
            case SOUTH: {
                GL11.glRotatef(270.0F, 0F, 1F, 0F);
                break;
            }
            case WEST: {
                GL11.glRotatef(180.0F, 0F, 1F, 0F);
                break;
            }
            case EAST: {
                GL11.glRotatef(0.0F, 0F, 1F, 0F);
                break;
            }
        }
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);

        Minecraft.getMinecraft().renderEngine.bindTexture(TEXTURE);

        MODEL.renderAnchored();
        double dist;
        if (smasher.extendedTicks <= 5) {
            dist = (Math.sin(Math.toRadians(18D * smasher.extendedTicks)) * 0.51F);
        } else if (smasher.extendedTicks <= 15) {
            dist = 0.51F;
        } else {
            dist = 0.51F - (Math.sin(Math.toRadians(9D * (smasher.extendedTicks - 15))) * 0.51F);
        }
        GL11.glTranslated(dist, 0.0F, 0.0F);
        MODEL.renderPiston(0.0F);

        if (smasher.spinup >= 41 && smasher.extendedTicks < 3 && smasher.getBlockMetadata() % 2 == 0) {
            GL11.glTranslated(-dist, 0.0F, 0.0F);
            GL11.glTranslatef(1.5F, 1.0F, 1.0F);
            GL11.glScaled(1.0F - dist * 2F, 1.0F, 1.0F);
            GL11.glTranslatef(-1.5F, -1.0F, -1.0F);
            GL11.glTranslatef(1.0F, 0.0F, 0.0F);

            GL11.glTranslatef((float) -smasher.getPos().getX(), (float) -smasher.getPos().getY(), (float) -smasher.getPos().getY());

            GL11.glPushMatrix();
            Tessellator tess = Tessellator.getInstance();
            Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            tess.draw();
            GL11.glPopMatrix();
        }
        GL11.glPopMatrix();
    }

    @Override
    public void renderInventoryTileEntityAt(TileEntity var1, double x, double y, double z, float var8) {
        GL11.glPushMatrix();
        GL11.glTranslatef((float) x, (float) y - 0.1F, (float) z);
        Minecraft.getMinecraft().renderEngine.bindTexture(TEXTURE);

        MODEL.renderAnchored();
        MODEL.renderPiston(0);
        GL11.glPopMatrix();
    }
}
