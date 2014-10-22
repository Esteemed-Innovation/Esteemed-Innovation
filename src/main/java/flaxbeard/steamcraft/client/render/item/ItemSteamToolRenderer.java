package flaxbeard.steamcraft.client.render.item;

import flaxbeard.steamcraft.common.item.tool.steam.ItemSteamAxe;
import flaxbeard.steamcraft.common.item.tool.steam.ItemSteamDrill;
import flaxbeard.steamcraft.common.item.tool.steam.ItemSteamShovel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;

public class ItemSteamToolRenderer implements IItemRenderer {

    private static RenderItem renderItem = new RenderItem();
    private final int toolType;

    public ItemSteamToolRenderer(int type) {
        this.toolType = type;
    }

    @Override
    public boolean handleRenderType(ItemStack itemStack, ItemRenderType type) {
        return type == ItemRenderType.INVENTORY;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item,
                                         ItemRendererHelper helper) {
        return false;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack itemStack, Object... data) {

        if (!itemStack.hasTagCompound()) {
            itemStack.setTagCompound(new NBTTagCompound());
            itemStack.stackTagCompound.setInteger("player", -1);
        }
        IIcon icon = itemStack.getIconIndex();
        int oldPlayer = itemStack.stackTagCompound.getInteger("player");
        if (oldPlayer != -1) {
            Entity player = Minecraft.getMinecraft().thePlayer.worldObj.getEntityByID(oldPlayer);
            if (player != null && player instanceof EntityPlayer) {
                icon = itemStack.getItem().getIcon(itemStack, 0, (EntityPlayer) player, itemStack, 0);
            }
        }

        GL11.glEnable(GL11.GL_ALPHA_TEST);
        renderItem.renderIcon(0, 0, icon, 16, 16);
        GL11.glDisable(GL11.GL_ALPHA_TEST);

        if (oldPlayer != -1) {
            Entity player = Minecraft.getMinecraft().thePlayer.worldObj.getEntityByID(oldPlayer);
            if (player != null && player instanceof EntityPlayer) {
                int use = 0;
                switch (toolType) {
                    case 0:
                        use = ItemSteamDrill.stuff.get(oldPlayer).right;
                        break;
                    case 1:
                        use = ItemSteamAxe.stuff.get(oldPlayer).right;
                        break;
                    case 2:
                        use = ItemSteamShovel.stuff.get(oldPlayer).right;
                        break;
                }

                double health = (double) (1000.0D - use) / 1000.0D;
                if (use > 0 && itemStack == Minecraft.getMinecraft().thePlayer.getHeldItem()) {
                    GL11.glPushMatrix();
                    int j1 = (int) Math.round(13.0D - health * 13.0D);
                    int k = (int) Math.round(255.0D - health * 255.0D);
                    GL11.glDisable(GL11.GL_LIGHTING);
                    GL11.glDisable(GL11.GL_DEPTH_TEST);
                    GL11.glDisable(GL11.GL_TEXTURE_2D);
                    GL11.glDisable(GL11.GL_ALPHA_TEST);
                    GL11.glDisable(GL11.GL_BLEND);
                    Tessellator tessellator = Tessellator.instance;
                    int l = 255 - k << 16 | k << 8;
                    int i1 = (255 - k) / 4 << 16 | 16128;
                    this.renderQuad(tessellator, 2, 10, 13, 2, 0);
                    this.renderQuad(tessellator, 2, 10, 12, 1, i1);
                    this.renderQuad(tessellator, 2, 10, j1, 1, l);
                    GL11.glEnable(GL11.GL_TEXTURE_2D);
                    GL11.glEnable(GL11.GL_LIGHTING);
                    GL11.glEnable(GL11.GL_DEPTH_TEST);
                    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                    GL11.glPopMatrix();
                }
            }
        }
    }

    private void renderQuad(Tessellator par1Tessellator, int par2, int par3, int par4, int par5, int par6) {
        par1Tessellator.startDrawingQuads();
        par1Tessellator.setColorOpaque_I(par6);
        par1Tessellator.addVertex((double) (par2 + 0), (double) (par3 + 0), 0.0D);
        par1Tessellator.addVertex((double) (par2 + 0), (double) (par3 + par5), 0.0D);
        par1Tessellator.addVertex((double) (par2 + par4), (double) (par3 + par5), 0.0D);
        par1Tessellator.addVertex((double) (par2 + par4), (double) (par3 + 0), 0.0D);
        par1Tessellator.draw();
    }

}
