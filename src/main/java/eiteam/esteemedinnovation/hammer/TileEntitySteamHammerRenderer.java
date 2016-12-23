package eiteam.esteemedinnovation.hammer;

import eiteam.esteemedinnovation.commons.visual.IInventoryTESR;
import eiteam.esteemedinnovation.commons.EsteemedInnovation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class TileEntitySteamHammerRenderer extends TileEntitySpecialRenderer implements IInventoryTESR {
    private static final ModelHammer MODEL = new ModelHammer();
    private static final ResourceLocation TEXTURE = new ResourceLocation(EsteemedInnovation.MOD_ID + ":textures/models/hammer.png");
    private static final float PX = (1.0F / 16.0F);

    @Override
    public void renderTileEntityAt(TileEntity te, double x, double y, double z, float partialTicks, int destroyStage) {
        GameSettings settings = Minecraft.getMinecraft().gameSettings;

        TileEntitySteamHammer hammer = (TileEntitySteamHammer) te;
        int meta = hammer.getBlockMetadata();
        ////EsteemedInnovation.log.debug(meta);
        GL11.glPushMatrix();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glTranslated(x, y, z);

        Minecraft.getMinecraft().renderEngine.bindTexture(TEXTURE);
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        GL11.glRotatef(90.0F * (meta + (meta % 2 * 2)), 0F, 1F, 0F);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        MODEL.renderNoRotate();
        GL11.glTranslatef(0, 10 * PX, PX);
        int ticks = getTicks(hammer.hammerTicks);
        float sin = MathHelper.sin((float) Math.toRadians(ticks - 60));
        GL11.glRotatef(40 + sin * (42.5F + 9.5F * (1 - sin)) - 52F, 1F, 0F, 0F);
        GL11.glPushMatrix();
        GL11.glScalef(1.0F, 1.0F, (13.0F + ((int) (sin * 42.5F - 42.5F)) / 9.0F) / 13.0F);
        MODEL.renderConnector((int) (sin * 42.5F - 42.5F));
        GL11.glPopMatrix();

        GL11.glRotatef(40 + sin * (42.5F + 9.5F * (1 - sin)) - 52F, -1F, 0F, 0F);
        GL11.glTranslatef(0, -6 * PX, 0);
        GL11.glRotatef(10 + sin * 42.5F - 42.5F, 1F, 0F, 0F);

        MODEL.render();
        GL11.glPopMatrix();

        ItemStack stackInSlotZero = hammer.getStackInSlot(0);
        if (stackInSlotZero != null) {
            GL11.glPushMatrix();
            GL11.glTranslatef((float) x + 0.5F, (float) y + (1.0F / 16.0F) - 0.02F, (float) z + 0.5F);
            GL11.glScalef(2.0F, 2.0F, 2.0F);
            GL11.glRotatef(0.0F + 90.0F * meta + (meta % 2 == 0 ? 180.0F : 0.0F), 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
            GL11.glTranslatef(0.0F, -(7.0F / 32.0F), 0.0F);
            ItemStack copy = stackInSlotZero.copy();
            copy.stackSize = 1;
            EntityItem item = new EntityItem(te.getWorld(), 0.0F, 0.0F, 0.0F, copy);
            item.hoverStart = 0.0F;
            boolean fancy = settings.fancyGraphics;
            settings.fancyGraphics = true;
            Minecraft.getMinecraft().getRenderManager().doRenderEntity(item, 0D, 0D, 0D, 0F, 0F, false);
            settings.fancyGraphics = fancy;
            GL11.glPopMatrix();
        }
    }

    @Override
    public void renderInventoryTileEntityAt(TileEntity tile, double x, double y, double z, float var8) {
        GL11.glPushMatrix();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glTranslated(x, y, z);

        Minecraft.getMinecraft().renderEngine.bindTexture(TEXTURE);
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        GL11.glRotatef(450F, 0F, 1F, 0F);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        MODEL.renderNoRotate();
        GL11.glTranslatef(0, 10 * PX, PX);
        int ticks = getTicks(Minecraft.getMinecraft().thePlayer.ticksExisted * 5);
        float sin = MathHelper.sin((float) Math.toRadians(ticks - 60));
        GL11.glRotatef(40 + sin * (42.5F + 9.5F * (1 - sin)) - 52F, 1F, 0F, 0F);
        GL11.glPushMatrix();
        GL11.glScalef(1.0F, 1.0F, (13.0F + ((int) (sin * 42.5F - 42.5F)) / 9.0F) / 13.0F);
        MODEL.renderConnector((int) (sin * 42.5F - 42.5F));
        GL11.glPopMatrix();
        GL11.glRotatef(40 + sin * (42.5F + 9.5F * (1 - sin)) - 52F, -1F, 0F, 0F);
        GL11.glTranslatef(0, -6 * PX, 0);
        GL11.glRotatef(10 + sin * 42.5F - 42.5F, 1F, 0F, 0F);

        MODEL.render();
        GL11.glPopMatrix();
    }

    private int getTicks(int startTicks) {
        int ticks = startTicks % 360;
        if (ticks <= 30) {
            return ticks * 6;
        } else if (ticks > 30 && ticks < 40) {
            return 180 + (ticks - 30);
        } else if (ticks >= 40 && ticks < 50) {
            return 180 + 10 - (ticks - 40);
        } else if (ticks >= 50 && ticks < 180) {
            return 180;
        }
        return ticks;
    }
}
