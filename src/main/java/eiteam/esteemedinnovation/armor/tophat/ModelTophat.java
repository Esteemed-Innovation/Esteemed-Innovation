package eiteam.esteemedinnovation.armor.tophat;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class ModelTophat extends ModelBiped {
    private ModelRenderer tophatBase;
    private ModelRenderer tophatHat;
    private int level;

    public ModelTophat() {
        super(1.0F, 0, 64, 32);
        tophatBase = new ModelRenderer(this, 64, 32).setTextureOffset(32, 0);
        tophatBase.addBox(-4.0F, -16.0F, -4.0F, 8, 7, 8);
        bipedHead.addChild(tophatBase);

        tophatHat = new ModelRenderer(this, 64, 32).setTextureOffset(0, 16);
        tophatHat.addBox(-5.5F, -9.0F, -5.5F, 11, 1, 11);
        bipedHead.addChild(tophatHat);
    }

    @Override
    public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entity);
        if (entity.isSneaking()) {
            GlStateManager.translate(0, 0.2F, 0);
        }
        bipedHead.render(scale);

        GlStateManager.pushMatrix();
        GlStateManager.translate(bipedHead.rotationPointX, bipedHead.rotationPointY, bipedHead.rotationPointZ);
        GlStateManager.rotate((float) Math.toDegrees(bipedHead.rotateAngleY), 0, 1, 0);
        GlStateManager.rotate((float) Math.toDegrees(bipedHead.rotateAngleX), 1, 0, 0);
        GlStateManager.rotate((float) Math.toDegrees(bipedHead.rotateAngleZ), 0, 0, 1);
        GlStateManager.translate(-bipedHead.rotationPointX, -bipedHead.rotationPointY, -bipedHead.rotationPointZ);
        ItemStack itemStack = new ItemStack(Items.EMERALD);
        if (level >= 18) {
            level = 18;
        }
        if (level >= 9) {
            level -= 8;
            itemStack = new ItemStack(Blocks.EMERALD_BLOCK);
        }
        for (int i = 0; i < level; i++) {
            GlStateManager.pushMatrix();
            EntityItem item = new EntityItem(entity.worldObj, 0.0F, 0.0F, 0.0F, itemStack);
            item.hoverStart = 0.0F;
            GlStateManager.rotate((float) (Minecraft.getMinecraft().thePlayer.ticksExisted * 10.0D) % 360 + (360F / level) * i, 0, 1, 0);
            GlStateManager.translate(0.75F, 0.0F, 0.0F);
            GlStateManager.rotate((float) (Minecraft.getMinecraft().thePlayer.ticksExisted * 11D) % 360, 0, 1, 0);
            Minecraft.getMinecraft().getRenderManager().doRenderEntity(item, 0.0D, -1.0D + 0.25F * Math.sin(Math.toRadians((Minecraft.getMinecraft().thePlayer.ticksExisted * 5) % 360) + (360F / level) * i), 0.0D, 0.0F, 0.0F, true);
            GlStateManager.popMatrix();
        }
        GlStateManager.popMatrix();
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
