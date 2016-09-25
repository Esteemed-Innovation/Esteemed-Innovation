package eiteam.esteemedinnovation.client.render.tile;

import eiteam.esteemedinnovation.EsteemedInnovation;
import eiteam.esteemedinnovation.block.BlockChargingPad;
import eiteam.esteemedinnovation.client.render.model.ModelChargingPad;
import eiteam.esteemedinnovation.tile.TileEntityChargingPad;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class TileEntityChargingPadRenderer extends TileEntitySpecialRenderer implements IInventoryTESR {
    private static final ModelChargingPad MODEL = new ModelChargingPad();
    private static final ResourceLocation TEXTURE = new ResourceLocation(EsteemedInnovation.MOD_ID + ":textures/models/charger.png");
    private static final int INVENTORY_ROTATION = 180;

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float partialTicks, int destroyStage) {
        TileEntityChargingPad pad = (TileEntityChargingPad) tileEntity;

        GL11.glPushMatrix();
        GL11.glTranslatef((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F);
        EnumFacing facing = tileEntity.getWorld().getBlockState(tileEntity.getPos()).getValue(BlockChargingPad.FACING);
        int rotation = 0;
        switch (facing) {
            case NORTH: {
                rotation = 90;
                break;
            }
            case SOUTH: {
                rotation = 270;
                break;
            }
            case WEST: {
                rotation = 180;
                break;
            }
            case EAST: {
                rotation = 0;
                break;
            }
            default: {
                break;
            }
        }

        GL11.glRotatef(90.0F, 0F, 1F, 0F);

        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        Minecraft.getMinecraft().renderEngine.bindTexture(TEXTURE);


        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        GL11.glRotatef(rotation, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);

        GL11.glTranslated(0.0F, 0.32D + 0.95D * Math.sin(Math.toRadians((90D / 40D) * pad.extendTicks)), 0.0F);
        Minecraft.getMinecraft().renderEngine.bindTexture(TEXTURE);

        MODEL.render(pad.extendTicks);

        GL11.glPopMatrix();
    }

    @Override
    public void renderInventoryTileEntityAt(TileEntity tileEntity, double x, double y, double z, float var8) {
        GL11.glPushMatrix();
        GL11.glRotatef(90.0F, 0F, 1F, 0F);

        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        //System.out.println(pad.extendTicks);
        Minecraft.getMinecraft().renderEngine.bindTexture(TEXTURE);

        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        GL11.glRotatef(INVENTORY_ROTATION, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);

        GL11.glTranslated(0.0F, 0.32D + 0.95D * Math.sin(Math.toRadians((90D / 40D) * 0)), 0.0F);
        Minecraft.getMinecraft().renderEngine.bindTexture(TEXTURE);

        MODEL.render(0);

        GL11.glPopMatrix();
    }
}
