package flaxbeard.steamcraft.client.render;

import flaxbeard.steamcraft.client.render.model.ModelThumper;
import flaxbeard.steamcraft.tile.TileEntityThumper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class TileEntityThumperRenderer extends TileEntitySpecialRenderer implements IInventoryTESR {
    private static final ModelThumper model = new ModelThumper();

    private static final ResourceLocation texture = new ResourceLocation("steamcraft:textures/models/thumper.png");

    @Override
    public void renderTileEntityAt(TileEntity var1, double var2, double var4, double var6, float var8) {
        TileEntityThumper thumper = (TileEntityThumper) var1;

        GL11.glPushMatrix();
        GL11.glTranslatef((float) var2 + 0.5F, (float) var4 + 0.5F, (float) var6 + 0.5F);
        int meta = var1.getWorldObj().getBlockMetadata(var1.xCoord, var1.yCoord, var1.zCoord);
        if (meta == 1 || meta == 3) {
            GL11.glRotatef(90.0F, 0F, 1F, 0F);
        }
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);

        Minecraft.getMinecraft().renderEngine.bindTexture(texture);
        model.render();
        double trans = 0;
        if (thumper.progress < 20) {
            trans = 0.0D;
        } else if (thumper.progress < 80) {
            trans = (3.0D * Math.sin(Math.toRadians(1.5D * (thumper.progress - 20))));
        } else if (thumper.progress < 90) {
            trans = 3.0D - (0.1D * Math.sin(Math.toRadians(9D * (thumper.progress - 80))));

        } else if (thumper.progress < 100) {
            trans = 2.9D;
        } else if (thumper.progress >= 100) {
            trans = 2.9D - 2.9D * (1.0D - Math.sin(Math.toRadians(90.0D - (9.0D * (thumper.progress - 100)))));
        }
        GL11.glTranslated(0.0F, trans, 0.0F);

        model.renderThumper();
        GL11.glPopMatrix();

    }


    @Override
    public void renderInventoryTileEntityAt(TileEntity var1, double x, double y, double z, float var8) {
        GL11.glPushMatrix();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glTranslated(x, y, z);

        Minecraft.getMinecraft().renderEngine.bindTexture(texture);
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        GL11.glScalef(0.3F, 0.3F, 0.3F);
        GL11.glTranslatef(-0.5F, -2.9F, -0.5F);


        model.render();
        model.renderThumper();

        GL11.glPopMatrix();

    }

}
