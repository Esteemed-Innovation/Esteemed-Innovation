package flaxbeard.steamcraft.client.render.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

public class ModelTophat extends ModelBiped {

    private ModelRenderer tophatBase;
    private ModelRenderer tophatHat;

    public int level;

    public ModelTophat() {
        super(1.0F, 0, 64, 32);
        tophatBase = new ModelRenderer(this, 64, 32).setTextureOffset(32, 0);
        tophatBase.addBox(-4.0F, -16.0F, -4.0F, 8, 7, 8);
        this.bipedHead.addChild(tophatBase);

        tophatHat = new ModelRenderer(this, 64, 32).setTextureOffset(0, 16);
        tophatHat.addBox(-5.5F, -9.0F, -5.5F, 11, 1, 11);
        this.bipedHead.addChild(tophatHat);
    }

    @Override
    public void render(Entity entity, float par2, float par3, float par4, float par5, float par6, float par7) {

        this.setRotationAngles(par2, par3, par4, par5, par6, par7, entity);

        this.bipedHead.render(par7);

        GL11.glPushMatrix();
        GL11.glTranslatef(this.bipedHead.rotationPointX, this.bipedHead.rotationPointY, this.bipedHead.rotationPointZ);
        GL11.glRotated(Math.toDegrees(this.bipedHead.rotateAngleY), 0, 1, 0);
        GL11.glRotated(Math.toDegrees(this.bipedHead.rotateAngleX), 1, 0, 0);
        GL11.glRotated(Math.toDegrees(this.bipedHead.rotateAngleZ), 0, 0, 1);
        GL11.glTranslatef(-this.bipedHead.rotationPointX, -this.bipedHead.rotationPointY, -this.bipedHead.rotationPointZ);
        ItemStack itemStack = new ItemStack(Items.emerald);
        if (level >= 18) {
            level = 18;
        }
        if (level >= 9) {
            level -= 8;
            itemStack = new ItemStack(Blocks.emerald_block);
        }
        for (int i = 0; i < level; i++) {
            GL11.glPushMatrix();
            EntityItem item = new EntityItem(entity.worldObj, 0.0F, 0.0F, 0.0F, itemStack);
            item.hoverStart = 0.0F;
            GL11.glRotated((Minecraft.getMinecraft().thePlayer.ticksExisted * 10.0D) % 360 + (360F / level) * i, 0, 1, 0);
            GL11.glTranslatef(0.75F, 0.0F, 0.0F);
            GL11.glRotated((Minecraft.getMinecraft().thePlayer.ticksExisted * 11D) % 360, 0, 1, 0);
            RenderManager.instance.renderEntityWithPosYaw(item, 0.0D, -1.0D + 0.25F * Math.sin(Math.toRadians((Minecraft.getMinecraft().thePlayer.ticksExisted * 5) % 360) + (360F / level) * i), 0.0D, 0.0F, 0.0F);
            item = null;
            GL11.glPopMatrix();
        }
        GL11.glPopMatrix();

    }

    @Override
    public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity par7Entity) {
        EntityLivingBase living = (EntityLivingBase) par7Entity;
        aimedBow = false;
        isSneak = living != null ? living.isSneaking() : false;
        if (living != null && living instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) living;

            ItemStack itemstack = player.inventory.getCurrentItem();
            heldItemRight = itemstack != null ? 1 : 0;

            if (itemstack != null && player.getItemInUseCount() > 0) {
                EnumAction enumaction = itemstack.getItemUseAction();
                if (enumaction == EnumAction.block) {
                    heldItemRight = 3;
                } else if (enumaction == EnumAction.bow) {
                    aimedBow = true;
                }
            }
        }
        super.setRotationAngles(par1, par2, par3, par4, par5, par6, par7Entity);
    }
}
