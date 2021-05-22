package eiteam.esteemedinnovation.transport.item;

import eiteam.esteemedinnovation.commons.EsteemedInnovation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderMortarItem extends Render<EntityMortarItem> {
    private static final ResourceLocation texture = new ResourceLocation(EsteemedInnovation.MOD_ID + ":textures/models/mortar_item.png");
    private static final ResourceLocation b4 = new ResourceLocation("minecraft:textures/blocks/destroy_stage_4.png");
    private static final ResourceLocation b5 = new ResourceLocation("minecraft:textures/blocks/destroy_stage_5.png");
    private static final ResourceLocation b6 = new ResourceLocation("minecraft:textures/blocks/destroy_stage_6.png");
    private static final ResourceLocation b7 = new ResourceLocation("minecraft:textures/blocks/destroy_stage_2.png");
    private static final ResourceLocation b8 = new ResourceLocation("minecraft:textures/blocks/destroy_stage_3.png");
    private static final ResourceLocation[] breakTextures = {b4, b5, b6, b7, b8};
    private static final ModelMortarItem model = new ModelMortarItem();

    public RenderMortarItem(RenderManager renderManager) {
        super(renderManager);
    }

    @Override
    public void doRender(EntityMortarItem myItem, double x, double y, double z, float entityYaw, float partialTicks) {
        GL11.glPushMatrix();
        GL11.glTranslated(x, y + 0.15f, z);

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
        EntityItem item = new EntityItem(myItem.world, 0.0F, 0.0F, 0.0F, myItem.getItem());
        item.hoverStart = 0.0F;

        GL11.glTranslated(0, 0.85F, 0);
        if (!renderManager.options.fancyGraphics) {
            GL11.glScalef(1.25F, 1.25F, 1.25F);
        }
        if (renderManager.options.fancyGraphics || item.getItem().getItem() instanceof ItemBlock) {
            GL11.glRotatef(Minecraft.getMinecraft().player.ticksExisted * 3 % 360, 0.0F, 1.0F, 0.0F);
        }
        renderManager.renderEntity(item, 0D, myItem.motionY <= 0.01 ? 0D : -3D, 0D, 0F, 0F, false);
        GL11.glPopMatrix();
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityMortarItem var1) {
        return null;
    }
}