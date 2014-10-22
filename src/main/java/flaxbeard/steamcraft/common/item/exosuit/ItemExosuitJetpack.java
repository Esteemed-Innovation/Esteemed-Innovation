package flaxbeard.steamcraft.common.item.exosuit;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flaxbeard.steamcraft.api.exosuit.ExosuitSlot;
import flaxbeard.steamcraft.client.render.model.ModelExosuit;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class ItemExosuitJetpack extends ItemExosuitUpgrade {
    public ResourceLocation tankTexture = new ResourceLocation("steamcraft:textures/models/armor/exo_3.png");
    public ResourceLocation test = new ResourceLocation("steamcraft:textures/models/mortarItem.png");

    public ItemExosuitJetpack() {
        super(ExosuitSlot.bodyFront, "", "", 0);
    }


    @Override
    public boolean hasOverlay() {
        return false;
    }

    @Override
    public boolean hasModel() {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void renderModel(ModelExosuit model, Entity entity, int armor, float size, ItemStack me) {
        if (armor == 1) {
            Minecraft.getMinecraft().renderEngine.bindTexture(tankTexture);
            ModelRenderer Jetpack1 = new ModelRenderer(model, 28, 0);
            ModelRenderer Jetpack2 = new ModelRenderer(model, 28, 0);

            Jetpack1.addBox(-7.0F, -2F, 3F, 4, 14, 4);
            model.bipedBody.addChild(Jetpack1);

            Jetpack2.addBox(3.0F, -2F, 3F, 4, 14, 4);
            model.bipedBody.addChild(Jetpack2);

            Jetpack1.render(size);
            Jetpack2.render(size);
            Jetpack1 = null;
            Jetpack2 = null;
//			Minecraft.getMinecraft().renderEngine.bindTexture(test);
//			ModelRenderer armGuard1 = new ModelRenderer(model, 28, 0);
//			armGuard1.addBox(-4.0F, -3F, -3F, 1, 1, 6);
//			model.bipedBody.addChild(armGuard1);
//			armGuard1.setRotationPoint(-5.0F, 2.0F, 0.0F);
//			armGuard1.rotateAngleY = model.bipedRightArm.rotateAngleY;
//			armGuard1.rotateAngleX = model.bipedRightArm.rotateAngleX;
//			armGuard1.rotateAngleZ = model.bipedRightArm.rotateAngleZ;
//			armGuard1.render(size);
//			ModelRenderer armGuard2 = new ModelRenderer(model, 28, 0);
//			armGuard2.addBox(-4.0F, -5.0F, -3F, 5, 1,  6);
//			model.bipedBody.addChild(armGuard2);
//			armGuard2.setRotationPoint(-5.0F, 2.0F, 0.0F);
//			armGuard2.rotateAngleY = model.bipedRightArm.rotateAngleY;
//			armGuard2.rotateAngleX = model.bipedRightArm.rotateAngleX;
//			armGuard2.rotateAngleZ = model.bipedRightArm.rotateAngleZ+(float)Math.toRadians(-70);
//			armGuard2.render(size);
//			armGuard1 = null;
//			armGuard2 = null;
        }
    }

}
