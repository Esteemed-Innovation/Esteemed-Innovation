package flaxbeard.steamcraft.client.render;

import flaxbeard.steamcraft.block.BlockValvePipe;
import flaxbeard.steamcraft.client.render.model.ModelValve;
import flaxbeard.steamcraft.tile.TileEntityValvePipe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;

public class TileEntityValvePipeRenderer extends TileEntitySpecialRenderer implements IInventoryTESR {
    private static final ModelValve model = new ModelValve();
    private static final ResourceLocation texture = new ResourceLocation("steamcraft:textures/blocks/blockCopper.png");


    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float partialTicks, int destroyStage) {
        TileEntityValvePipe valve = (TileEntityValvePipe) tileEntity;
        GL11.glPushMatrix();
        GL11.glTranslatef((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F);
        EnumFacing facing = tileEntity.getWorld().getBlockState(tileEntity.getPos()).getValue(BlockValvePipe.FACING);
        float outset = 3.5F + (valve.open ? 0.0F : -1.0F) + (valve.open ? -0.1F * valve.turnTicks : 0.1F * valve.turnTicks);
        GL11.glTranslatef(outset * facing.getFrontOffsetX() / 16.0F, outset * facing.getFrontOffsetY() / 16.0F,
          outset * facing.getFrontOffsetZ() / 16.0F);
        // Why is there no WEST rotation? ...
        switch (facing) {
            case UP: {
                GL11.glRotatef(-90, 0, 0, 1);
            }
            case DOWN: {
                GL11.glRotatef(90, 0, 0, 1);
            }
            case NORTH: {
                GL11.glRotated(-90F, 0, 1, 0);
            }
            case SOUTH: {
                GL11.glRotated(90F, 0, 1, 0);
            }
            case EAST: {
                GL11.glRotated(180F, 0, 1, 0);
            }
        }
        GL11.glRotated((225.0F * (valve.isOpen() ? valve.turnTicks : 10 - valve.turnTicks) / 10.0F), 1, 0, 0);
        Minecraft.getMinecraft().renderEngine.bindTexture(texture);

        model.render();
        GL11.glPopMatrix();
    }


    @Override
    public void renderInventoryTileEntityAt(TileEntity tileEntity, double x, double y, double z, float var8) {}

}
