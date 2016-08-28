package eiteam.esteemedinnovation.client.render.tile;

import eiteam.esteemedinnovation.EsteemedInnovation;
import eiteam.esteemedinnovation.client.render.model.ModelMortar;
import eiteam.esteemedinnovation.tile.TileEntityItemMortar;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class TileEntityItemMortarRenderer extends TileEntitySpecialRenderer implements IInventoryTESR {
    private static final ModelMortar MODEL = new ModelMortar();
    private static final ResourceLocation TEXTURE = new ResourceLocation(EsteemedInnovation.MOD_ID + ":textures/models/itemMortar.png");
    private static final float PX = (1.0F / 16.0F);

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float partialTicks, int destroyStage) {
        TileEntityItemMortar mortar = (TileEntityItemMortar) tile;
        GL11.glPushMatrix();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glTranslated(x, y, z);

        Minecraft.getMinecraft().renderEngine.bindTexture(TEXTURE);
        MODEL.renderBase();
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        if (mortar.zTarget != mortar.getPos().getZ()) {
            GL11.glRotated(Math.toDegrees((float) Math.atan((float) (mortar.xTarget - mortar.getPos().getZ()) / (float) (mortar.zTarget - mortar.getPos().getZ()))), 0F, 1F, 0F);
        } else {
            GL11.glRotated(270.0F, 0F, 1F, 0F);
        }
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        MODEL.render();
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        GL11.glRotated(2.0F, 1F, 0F, 0F);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        if (mortar.fireTicks > 60 && mortar.fireTicks <= 65) {
            GL11.glTranslated(0.0F, -0.05F * Math.sin(Math.PI * ((mortar.fireTicks - 60) / 10.0F)), 0.0F);
        } else if (mortar.fireTicks > 65 && mortar.fireTicks < 80) {
            GL11.glTranslated(0.0F, -0.05F * (1 - Math.sin(Math.PI * ((mortar.fireTicks - 65) / 30.0F))), 0.0F);
        }
        MODEL.renderCannon1();
        if (mortar.fireTicks > 60 && mortar.fireTicks <= 65) {
            GL11.glTranslated(0.0F, -0.15F * Math.sin(Math.PI * ((mortar.fireTicks - 60) / 10.0F)), 0.0F);
        } else if (mortar.fireTicks > 65 && mortar.fireTicks < 80) {
            GL11.glTranslated(0.0F, -0.15F * (1 - Math.sin(Math.PI * ((mortar.fireTicks - 65) / 30.0F))), 0.0F);
        }
        MODEL.renderCannon2();
        GL11.glPopMatrix();
        GL11.glRotated(0, 180, 0, 0);
    }

    @Override
    public void renderInventoryTileEntityAt(TileEntity tile, double x, double y, double z, float partialTicks) {
        GL11.glPushMatrix();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glTranslated(x, y, z);

        Minecraft.getMinecraft().renderEngine.bindTexture(TEXTURE);
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        GL11.glScalef(0.5F, 0.5F, 0.5F);
        GL11.glTranslatef(-0.5F, -1.5F, -0.5F);
        MODEL.renderBase();
        MODEL.render();
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        GL11.glRotated(2.0F, 1F, 0F, 0F);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        MODEL.renderCannon1();
        MODEL.renderCannon2();
        GL11.glPopMatrix();
    }
}
