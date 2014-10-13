package flaxbeard.steamcraft.client.render;

import flaxbeard.steamcraft.client.render.model.ModelMortar;
import flaxbeard.steamcraft.tile.TileEntityItemMortar;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class TileEntityItemMortarRenderer extends TileEntitySpecialRenderer implements IInventoryTESR {

    private static final ModelMortar model = new ModelMortar();
    private static final ResourceLocation texture = new ResourceLocation("steamcraft:textures/models/itemMortar.png");
    private static float px = (1.0F / 16.0F);


    @Override
    public void renderTileEntityAt(TileEntity var1, double x, double y,
                                   double z, float var8) {

        GameSettings settings = Minecraft.getMinecraft().gameSettings;

        TileEntityItemMortar mortar = (TileEntityItemMortar) var1;
        int meta = mortar.getWorldObj().getBlockMetadata(var1.xCoord, var1.yCoord, var1.zCoord);
        GL11.glPushMatrix();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glTranslated(x, y, z);

        Minecraft.getMinecraft().renderEngine.bindTexture(texture);
        model.renderBase();
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        if (mortar.zT != mortar.zCoord) {
            GL11.glRotated(Math.toDegrees((float) Math.atan((float) (mortar.xT - mortar.xCoord) / (float) (mortar.zT - mortar.zCoord))), 0F, 1F, 0F);
        } else {
            GL11.glRotated(270.0F, 0F, 1F, 0F);
        }
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        model.render();
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        GL11.glRotated(2.0F, 1F, 0F, 0F);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        if (mortar.fireTicks > 60 && mortar.fireTicks <= 65) {
            GL11.glTranslated(0.0F, -0.05F * Math.sin(Math.PI * ((mortar.fireTicks - 60) / 10.0F)), 0.0F);
        } else if (mortar.fireTicks > 65 && mortar.fireTicks < 80) {
            GL11.glTranslated(0.0F, -0.05F * (1 - Math.sin(Math.PI * ((mortar.fireTicks - 65) / 30.0F))), 0.0F);
        }
        model.renderCannon1();
        if (mortar.fireTicks > 60 && mortar.fireTicks <= 65) {
            GL11.glTranslated(0.0F, -0.15F * Math.sin(Math.PI * ((mortar.fireTicks - 60) / 10.0F)), 0.0F);
        } else if (mortar.fireTicks > 65 && mortar.fireTicks < 80) {
            GL11.glTranslated(0.0F, -0.15F * (1 - Math.sin(Math.PI * ((mortar.fireTicks - 65) / 30.0F))), 0.0F);
        }
        model.renderCannon2();
        GL11.glPopMatrix();
        GL11.glRotated(0, 180, 0, 0);
    }

    @Override
    public void renderInventoryTileEntityAt(TileEntity var1, double x,
                                            double y, double z, float var8) {
        GameSettings settings = Minecraft.getMinecraft().gameSettings;

        //TileEntityItemMortar mortar = (TileEntityItemMortar) var1;
        GL11.glPushMatrix();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glTranslated(x, y, z);

        Minecraft.getMinecraft().renderEngine.bindTexture(texture);
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        GL11.glScalef(0.5F, 0.5F, 0.5F);
        GL11.glTranslatef(-0.5F, -1.5F, -0.5F);
        model.renderBase();
        model.render();
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        GL11.glRotated(2.0F, 1F, 0F, 0F);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        model.renderCannon1();
        model.renderCannon2();
        GL11.glPopMatrix();

    }

}
