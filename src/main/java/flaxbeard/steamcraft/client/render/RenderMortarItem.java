package flaxbeard.steamcraft.client.render;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flaxbeard.steamcraft.client.render.model.ModelMortarItem;
import flaxbeard.steamcraft.entity.EntityMortarItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderMortarItem extends Render {
    private static final ResourceLocation texture = new ResourceLocation("steamcraft:textures/models/mortarItem.png");
    private static final ResourceLocation b4 = new ResourceLocation("minecraft:textures/blocks/destroy_stage_4.png");
    private static final ResourceLocation b5 = new ResourceLocation("minecraft:textures/blocks/destroy_stage_5.png");
    private static final ResourceLocation b6 = new ResourceLocation("minecraft:textures/blocks/destroy_stage_6.png");
    private static final ResourceLocation b7 = new ResourceLocation("minecraft:textures/blocks/destroy_stage_2.png");
    private static final ResourceLocation b8 = new ResourceLocation("minecraft:textures/blocks/destroy_stage_3.png");
    private static final ResourceLocation[] breakTextures = {b4, b5, b6, b7, b8};
    private static final ModelMortarItem model = new ModelMortarItem();

    @Override
    public void doRender(Entity var1, double x, double y, double z, float var8, float var9) {
        GL11.glPushMatrix();
        GL11.glTranslated(x, y, z);
        EntityMortarItem myItem = (EntityMortarItem) var1;

        if (myItem.onGround && myItem.lastTickPosX == myItem.posX && myItem.lastTickPosZ == myItem.posZ) {
            Minecraft.getMinecraft().renderEngine.bindTexture(breakTextures[myItem.randomSprite]);
            model.renderMinePart();
        }

        if (myItem.motionY > 0) {
            GL11.glRotatef(180, 1.0F, 0.0F, 0.0F);
        }

        Minecraft.getMinecraft().renderEngine.bindTexture(texture);
        if (myItem.onGround && myItem.lastTickPosX == myItem.posX && myItem.lastTickPosZ == myItem.posZ) {
            GL11.glTranslatef(0.0F, -0.15F, 0.0F);
            GL11.glRotatef(myItem.randomDir, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(myItem.randomDir2, 1.0F, 0.0F, 0.0F);
        } else if (myItem.onGround) {
            GL11.glRotatef(myItem.randomDir, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(myItem.randomDir2, 1.0F, 0.0F, 0.0F);
        }
        model.renderAll();
        GL11.glRotatef(myItem.randomDir, 0.0F, -1.0F, 0.0F);
        if (myItem.motionY > 0) {
            GL11.glRotatef(180, 1.0F, 0.0F, 0.0F);
        }
        EntityItem item = new EntityItem(myItem.worldObj, 0.0F, 0.0F, 0.0F, myItem.getEntityItem());
        item.hoverStart = 0.0F;

        GL11.glTranslated(0, 0.85F, 0);
        if (!this.renderManager.options.fancyGraphics) {
            GL11.glScalef(1.25F, 1.25F, 1.25F);
        }
        if (this.renderManager.options.fancyGraphics || item.getEntityItem().getItem() instanceof ItemBlock) {
            GL11.glRotatef(Minecraft.getMinecraft().thePlayer.ticksExisted * 3 % 360, 0.0F, 1.0F, 0.0F);
        }
        RenderManager.instance.renderEntityWithPosYaw(item, 0.0D, 0.0D, 0.0D, 0.0F, 0.0F);
        GL11.glPopMatrix();

    }

    @Override
    protected ResourceLocation getEntityTexture(Entity var1) {
        return null;
    }

}