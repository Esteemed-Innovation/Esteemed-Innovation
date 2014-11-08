package flaxbeard.steamcraft.client.render;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flaxbeard.steamcraft.api.ICrucibleMold;
import flaxbeard.steamcraft.client.render.model.ModelMold;
import flaxbeard.steamcraft.tile.TileEntityMold;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

@SideOnly(Side.CLIENT)
public class TileEntityMoldRenderer extends TileEntitySpecialRenderer implements IInventoryTESR {

    private static final ModelMold model = new ModelMold();
    private static final ResourceLocation texture = new ResourceLocation("steamcraft:textures/models/mold.png");
    private static final ResourceLocation texture2 = new ResourceLocation("steamcraft:textures/models/mold2.png");
    private static float px = (1.0F / 16.0F);

    @Override
    public void renderTileEntityAt(TileEntity var1, double x, double y, double z, float var8) {

        TileEntityMold mold = (TileEntityMold) var1;
        int meta = mold.getWorldObj().getBlockMetadata(var1.xCoord, var1.yCoord, var1.zCoord);
        GL11.glPushMatrix();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glColor4f(1F, 1F, 1F, 1F);
        GL11.glTranslated(x, y, z);

        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        GL11.glRotatef(90.0F * (meta + (meta % 2 * 2)) + 180.0F, 0F, 1F, 0F);
        Minecraft.getMinecraft().renderEngine.bindTexture(texture);

        GL11.glScalef(1F, -1F, -1F);
        model.renderBottom();
        GL11.glPushMatrix();
        GL11.glRotatef(90.0F, 1F, 0F, 0F);
        float pix = 1.0F / 16.0F;
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F + 4 * pix);

        if (mold.mold[0] != null) {
            renderMold(mold.mold[0].getItem(), true);
        }
        GL11.glTranslatef(0.0F, 0.0F, -0.001F);
        renderMoldUnder();

        GL11.glPopMatrix();
        Minecraft.getMinecraft().renderEngine.bindTexture(texture);
        GL11.glScalef(1F, -1F, -1F);

        GL11.glScalef(1F, -1F, -1F);
        int ticks = Minecraft.getMinecraft().thePlayer.ticksExisted;
        float px = (1.0F / 16.0F);

        GL11.glTranslatef(0.0F * 90.0F, 4 * px, -6 * px);
        if (mold.open) {
            float tick = (float) (Math.PI * (mold.changeTicks * 90.0F / 20.0F)) / 180.0F;
            GL11.glRotatef(100.0F - MathHelper.sin(tick) * 100.0F, 1F, 0F, 0F);
        } else {
            float tick = (float) (Math.PI * (mold.changeTicks * 90.0F / 20.0F)) / 180.0F;
            GL11.glRotatef(0.0F + MathHelper.sin(tick) * 100.0F, 1F, 0F, 0F);
        }
        model.renderTop();
        GL11.glPushMatrix();
        GL11.glRotatef(270.0F, 1F, 0F, 0F);
        GL11.glTranslatef(-0.5F, -0.5F - 6 * pix, -0.5F + 8 * pix);

        if (mold.mold[0] != null) {
            renderMold(mold.mold[0].getItem(), false);
        }
        GL11.glTranslatef(0.0F, 0.0F, -0.001F);
        renderMoldUnder();
        GL11.glPopMatrix();
        Minecraft.getMinecraft().renderEngine.bindTexture(texture);

        GL11.glScalef(1F, -1F, -1F);

        GL11.glPopMatrix();
    }

    private void renderMold(Item item, boolean bottom) {
        Minecraft.getMinecraft().renderEngine.bindTexture(((ICrucibleMold) item).getBlockTexture());
        Tessellator tessellator = Tessellator.instance;
        float f1 = 0.0F;
        float f2 = 1.0F;
        float f3 = 1.0F;
        float f4 = 0.0F;
        float pix2 = 2.0F / 16.0F;
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, 1.0F);
        if (bottom) {
            tessellator.addVertexWithUV(0.0D, 0.0D, 0.0D, f1, f4);
            tessellator.addVertexWithUV(1.0D, 0.0D, 0.0D, f3, f4);
            tessellator.addVertexWithUV(1.0D, 1.0D, 0.0D, f3, f2);
            tessellator.addVertexWithUV(0.0D, 1.0D, 0.0D, f1, f2);
        } else {
            tessellator.addVertexWithUV(1.0D, 1.0D, 0.0D, f1, f4);
            tessellator.addVertexWithUV(0.0D, 1.0D, 0.0D, f3, f4);
            tessellator.addVertexWithUV(0.0D, 0.0D, 0.0D, f3, f2);
            tessellator.addVertexWithUV(1.0D, 0.0D, 0.0D, f1, f2);
        }
        tessellator.draw();
    }

    private void renderMoldUnder() {
        Minecraft.getMinecraft().renderEngine.bindTexture(texture2);
        Tessellator tessellator = Tessellator.instance;
        float f1 = 0.0F;
        float f2 = 1.0F;
        float f3 = 1.0F;
        float f4 = 0.0F;
        float pix2 = 2.0F / 16.0F;
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, 1.0F);
        tessellator.addVertexWithUV(0.0D, 0.0D, 0.0D, f1, f4);
        tessellator.addVertexWithUV(1.0D, 0.0D, 0.0D, f3, f4);
        tessellator.addVertexWithUV(1.0D, 1.0D, 0.0D, f3, f2);
        tessellator.addVertexWithUV(0.0D, 1.0D, 0.0D, f1, f2);
        tessellator.draw();
    }

    @Override
    public void renderInventoryTileEntityAt(TileEntity var1, double x, double y, double z, float var8) {
        TileEntityMold mold = (TileEntityMold) var1;
        GL11.glPushMatrix();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glColor4f(1F, 1F, 1F, 1F);
        GL11.glTranslated(x, y, z);

        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        Minecraft.getMinecraft().renderEngine.bindTexture(texture);

        GL11.glScalef(1F, -1F, -1F);
        model.renderBottom();
        GL11.glPushMatrix();
        GL11.glRotatef(90.0F, 1F, 0F, 0F);
        float pix = 1.0F / 16.0F;
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F + 4 * pix);
        GL11.glTranslatef(0.0F, 0.0F, -0.001F);

        GL11.glPopMatrix();
        Minecraft.getMinecraft().renderEngine.bindTexture(texture);
        GL11.glScalef(1F, -1F, -1F);

        GL11.glScalef(1F, -1F, -1F);
        float px = (1.0F / 16.0F);

        GL11.glTranslatef(0.0F * 90.0F, 4 * px, -6 * px);

        float tick = (float) (Math.PI * (0 * 90.0F / 20.0F)) / 180.0F;
        GL11.glRotatef(0.0F + MathHelper.sin(tick) * 100.0F, 1F, 0F, 0F);

        model.renderTop();
        GL11.glPushMatrix();
        GL11.glRotatef(270.0F, 1F, 0F, 0F);
        GL11.glTranslatef(-0.5F, -0.5F - 6 * pix, -0.5F + 8 * pix);
        GL11.glTranslatef(0.0F, 0.0F, -0.001F);

        GL11.glPopMatrix();
        Minecraft.getMinecraft().renderEngine.bindTexture(texture);

        GL11.glScalef(1F, -1F, -1F);

        GL11.glPopMatrix();
    }

}
