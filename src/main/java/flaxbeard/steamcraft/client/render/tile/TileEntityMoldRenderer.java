package flaxbeard.steamcraft.client.render.tile;

import flaxbeard.steamcraft.api.ICrucibleMold;
import flaxbeard.steamcraft.client.render.RenderUtility;
import flaxbeard.steamcraft.client.render.model.ModelMold;
import flaxbeard.steamcraft.tile.TileEntityMold;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

@SideOnly(Side.CLIENT)
public class TileEntityMoldRenderer extends TileEntitySpecialRenderer implements IInventoryTESR {
    private static final ModelMold MODEL = new ModelMold();
    private static final ResourceLocation TEXTURE = new ResourceLocation("steamcraft:textures/models/mold.png");
    private static final ResourceLocation TEXTURE_2 = new ResourceLocation("steamcraft:textures/models/mold2.png");
    private static final float PX = (1F / 16F);

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float partialTicks, int destroyStage) {
        TileEntityMold mold = (TileEntityMold) tile;
        int meta = tile.getBlockMetadata();
        GL11.glPushMatrix();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glColor4f(1F, 1F, 1F, 1F);
        GL11.glTranslated(x, y, z);

        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        GL11.glRotatef(90.0F * (meta + (meta % 2 * 2)) + 180.0F, 0F, 1F, 0F);
        Minecraft.getMinecraft().renderEngine.bindTexture(TEXTURE);

        GL11.glScalef(1F, -1F, -1F);
        MODEL.renderBottom();
        GL11.glPushMatrix();
        GL11.glRotatef(90.0F, 1F, 0F, 0F);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F + 4 * PX);

        if (mold.mold != null) {
            renderMold(mold.mold.getItem(), true);
        }

        GL11.glTranslatef(0.0F, 0.0F, -0.001F);
        renderMoldUnder();

        GL11.glPopMatrix();
        Minecraft.getMinecraft().renderEngine.bindTexture(TEXTURE);
        GL11.glScalef(1F, -1F, -1F);

        GL11.glScalef(1F, -1F, -1F);

        GL11.glTranslatef(0.0F * 90.0F, 4 * PX, -6 * PX);
        if (mold.isOpen) {
            float tick = (float) (Math.PI * (mold.changeTicks * 90.0F / 20.0F)) / 180.0F;
            GL11.glRotatef(100.0F - MathHelper.sin(tick) * 100.0F, 1F, 0F, 0F);
        } else {
            float tick = (float) (Math.PI * (mold.changeTicks * 90.0F / 20.0F)) / 180.0F;
            GL11.glRotatef(0.0F + MathHelper.sin(tick) * 100.0F, 1F, 0F, 0F);
        }
        MODEL.renderTop();
        GL11.glPushMatrix();
        GL11.glRotatef(270.0F, 1F, 0F, 0F);
        GL11.glTranslatef(-0.5F, -0.5F - 6 * PX, -0.5F + 8 * PX);

        if (mold.mold != null) {
            renderMold(mold.mold.getItem(), false);
        }
        GL11.glTranslatef(0.0F, 0.0F, -0.001F);
        renderMoldUnder();
        GL11.glPopMatrix();
        Minecraft.getMinecraft().renderEngine.bindTexture(TEXTURE);

        GL11.glScalef(1F, -1F, -1F);

        GL11.glPopMatrix();
    }

    private void renderMold(Item item, boolean bottom) {
        Minecraft.getMinecraft().renderEngine.bindTexture(((ICrucibleMold) item).getBlockTexture());
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer buffer = tessellator.getBuffer();
        buffer.putNormal(0F, 0F, 1F);
        if (bottom) {
            RenderUtility.addVertexWithUV(buffer, 0D, 0D, 0D, 0F, 0F);
            RenderUtility.addVertexWithUV(buffer, 1D, 0D, 0D, 1F, 0F);
            RenderUtility.addVertexWithUV(buffer, 1D, 1D, 0D, 1F, 1F);
            RenderUtility.addVertexWithUV(buffer, 0D, 1D, 0D, 0F, 1F);
        } else {
            RenderUtility.addVertexWithUV(buffer, 1D, 1D, 0D, 0F, 0F);
            RenderUtility.addVertexWithUV(buffer, 0D, 1D, 0D, 1F, 0F);
            RenderUtility.addVertexWithUV(buffer, 0D, 0D, 0D, 1F, 1F);
            RenderUtility.addVertexWithUV(buffer, 1D, 0D, 0D, 0F, 1F);
        }
        tessellator.draw();
    }

    private void renderMoldUnder() {
        Minecraft.getMinecraft().renderEngine.bindTexture(TEXTURE_2);
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer buffer = tessellator.getBuffer();
        buffer.putNormal(0F, 0F, 1F);
        RenderUtility.addVertexWithUV(buffer, 0.0D, 0.0D, 0.0D, 0F, 0F);
        RenderUtility.addVertexWithUV(buffer, 1.0D, 0.0D, 0.0D, 1F, 0F);
        RenderUtility.addVertexWithUV(buffer, 1.0D, 1.0D, 0.0D, 1F, 1F);
        RenderUtility.addVertexWithUV(buffer, 0.0D, 1.0D, 0.0D, 0F, 1F);
        tessellator.draw();
    }

    @Override
    public void renderInventoryTileEntityAt(TileEntity var1, double x, double y, double z, float partialTicks) {
        GL11.glPushMatrix();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glColor4f(1F, 1F, 1F, 1F);
        GL11.glTranslated(x, y, z);

        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        Minecraft.getMinecraft().renderEngine.bindTexture(TEXTURE);

        GL11.glScalef(1F, -1F, -1F);
        MODEL.renderBottom();
        GL11.glPushMatrix();
        GL11.glRotatef(90.0F, 1F, 0F, 0F);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F + 4 * PX);
        GL11.glTranslatef(0.0F, 0.0F, -0.001F);

        GL11.glPopMatrix();
        Minecraft.getMinecraft().renderEngine.bindTexture(TEXTURE);
        GL11.glScalef(1F, -1F, -1F);

        GL11.glScalef(1F, -1F, -1F);

        GL11.glTranslatef(0.0F * 90.0F, 4 * PX, -6 * PX);

        float tick = (float) (Math.PI * (0 * 90.0F / 20.0F)) / 180.0F;
        GL11.glRotatef(0.0F + MathHelper.sin(tick) * 100.0F, 1F, 0F, 0F);

        MODEL.renderTop();
        GL11.glPushMatrix();
        GL11.glRotatef(270.0F, 1F, 0F, 0F);
        GL11.glTranslatef(-0.5F, -0.5F - 6 * PX, -0.5F + 8 * PX);
        GL11.glTranslatef(0.0F, 0.0F, -0.001F);

        GL11.glPopMatrix();
        Minecraft.getMinecraft().renderEngine.bindTexture(TEXTURE);

        GL11.glScalef(1F, -1F, -1F);

        GL11.glPopMatrix();
    }
}
