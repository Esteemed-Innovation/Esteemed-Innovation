package flaxbeard.steamcraft.client.render.tile;

import flaxbeard.steamcraft.client.render.item.IInventoryTESR;
import flaxbeard.steamcraft.client.render.model.ModelSaw;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * Created by elijahfoster-wysocki on 10/19/14.
 */

public class TileEntitySawRenderer extends TileEntitySpecialRenderer implements IInventoryTESR {
    public static final ModelSaw model = new ModelSaw();

    private static final ResourceLocation textureLoc = new ResourceLocation("steamcraft:textures/models/saw.png");

    @Override
    public void renderTileEntityAt(TileEntity te, double par2, double par3, double par4, float par5){
        GL11.glPushMatrix();
    }

    @Override
    public void renderInventoryTileEntityAt(TileEntity te, double x, double y, double z, float fl){
        Minecraft.getMinecraft().renderEngine.bindTexture(textureLoc);
    }
}
