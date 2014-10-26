package flaxbeard.steamcraft.client.render;

import flaxbeard.steamcraft.client.render.model.ModelFan;
import flaxbeard.steamcraft.client.render.model.ModelSaw;
import flaxbeard.steamcraft.tile.TileEntitySaw;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

/**
 * Created by elijahfoster-wysocki on 10/19/14.
 */

public class TileEntitySawRenderer extends TileEntitySpecialRenderer implements IInventoryTESR {
    public static final ModelSaw model = new ModelSaw();

    private static final ResourceLocation textureLoc = new ResourceLocation("steamcraft:textures/models/saw.png");

    @Override
    public void renderTileEntityAt(TileEntity te, double par2, double par3, double par4, float par5){
        TileEntitySaw saw = (TileEntitySaw) te;

        GL11.glPushMatrix();
        GL11.glTranslatef((float) par2 + 0.5F, (float) par3 + 0.5F, (float) par4 + 0.5F);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);

        Minecraft.getMinecraft().renderEngine.bindTexture(textureLoc);

        model.render();
        GL11.glTranslated(0.0F, 0.0D, 0.0F);

        model.renderBlade();

        GL11.glPopMatrix();
    }

    @Override
    public void renderInventoryTileEntityAt(TileEntity te, double x, double y, double z, float fl){
        GL11.glPushMatrix();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glTranslated(x, y, z);

        Minecraft.getMinecraft().renderEngine.bindTexture(textureLoc);
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        GL11.glScalef(0.3F, 0.3F, 0.3F);
        GL11.glTranslatef(-0.5F, -2.9F, -0.5F);

        model.render();
        model.renderBlade();

        GL11.glPopMatrix();
    }
}
